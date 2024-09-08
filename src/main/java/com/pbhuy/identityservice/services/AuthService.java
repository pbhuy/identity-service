package com.pbhuy.identityservice.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pbhuy.identityservice.dto.request.AuthRequest;
import com.pbhuy.identityservice.dto.request.IntrospectRequest;
import com.pbhuy.identityservice.dto.request.LogoutRequest;
import com.pbhuy.identityservice.dto.request.RefreshRequest;
import com.pbhuy.identityservice.dto.response.AuthResponse;
import com.pbhuy.identityservice.dto.response.IntrospectResponse;
import com.pbhuy.identityservice.entities.InvalidToken;
import com.pbhuy.identityservice.entities.Role;
import com.pbhuy.identityservice.entities.User;
import com.pbhuy.identityservice.enums.ErrorCode;
import com.pbhuy.identityservice.exceptions.AppException;
import com.pbhuy.identityservice.repositories.InvalidTokenRepository;
import com.pbhuy.identityservice.repositories.RoleRepository;
import com.pbhuy.identityservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final InvalidTokenRepository invalidTokenRepository;
    private final RoleRepository roleRepository;

    @Value("${jwt.secret-key}")
    protected String SECRET;

    @Value("${jwt.validation-duration}")
    protected int JWT_VALIDATION_DURATION;

    @Value("${jwt.refreshable-duration}")
    protected int JWT_REFRESHABLE_DURATION;

    @Value("${bcrypt.salt-length}")
    protected int SALT_LENGTH;

    @Autowired
    public AuthService(UserRepository userRepository, InvalidTokenRepository invalidTokenRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.invalidTokenRepository = invalidTokenRepository;
        this.roleRepository = roleRepository;
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(SALT_LENGTH);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHORIZED);
        return AuthResponse.builder()
                .token(generateToken(user.getUsername(), user.getRoles()))
                .authenticated(true)
                .build();
    }

    public Set<Role> convertPermissions(List<String> roles) {
        return new HashSet<>(roleRepository.findAll());
    }

    public String generateToken(String username, Set<Role> roles) {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("pbhuy")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(JWT_VALIDATION_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(roles))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        // sign
        try {
            jwsObject.sign(new MACSigner(SECRET.getBytes()));
        } catch (JOSEException e) {
            log.error("Cannot sign JWT object", e);
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        String token = request.getToken();
        try {
            validateToken(token, false);
        } catch (AppException e) {
            throw new AppException(e.getErrorCode());
        }
        IntrospectResponse response = new IntrospectResponse();
        response.setValid(true);
        return response;
    }

    private SignedJWT validateToken(String token, boolean isRefresh)
            throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new MACVerifier(SECRET);
        Date expirationDate = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(JWT_REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();


        boolean verified = signedJWT.verify(verifier);
        if (!verified) {
            log.error("Verification failed");
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        boolean isExpired = expirationDate.after(new Date());
        if (!isExpired) {
            log.error("Token expired");
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            log.error("Token invalid");
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return signedJWT;
    }

    public AuthResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {
        SignedJWT signedJWT = validateToken(request.getToken(), true);

        // invalidate token
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        String username = signedJWT.getJWTClaimsSet().getSubject();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwtId)
                .expiryTime(expirationTime)
                .build();
        invalidTokenRepository.save(invalidToken);

        // refresh token
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        String token = generateToken(user.getUsername(), user.getRoles());

        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = validateToken(request.getToken(), true);
            InvalidToken invalidToken = InvalidToken.builder()
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .build();
            invalidTokenRepository.save(invalidToken);
        } catch (AppException e) {
            log.error("Token already expired", e);
        }
    }

    private String buildScope(Set<Role> roles) {
        StringJoiner joiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
            });
        }
        return joiner.toString();
    }
}
