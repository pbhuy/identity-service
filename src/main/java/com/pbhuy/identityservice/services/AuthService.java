package com.pbhuy.identityservice.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pbhuy.identityservice.dto.request.AuthRequest;
import com.pbhuy.identityservice.dto.request.IntrospectRequest;
import com.pbhuy.identityservice.dto.response.AuthResponse;
import com.pbhuy.identityservice.dto.response.IntrospectResponse;
import com.pbhuy.identityservice.entities.User;
import com.pbhuy.identityservice.enums.ErrorCode;
import com.pbhuy.identityservice.exceptions.AppException;
import com.pbhuy.identityservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    protected String SECRET;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHORIZED);
        AuthResponse response = new AuthResponse();
        response.setAuthenticated(true);
        response.setToken(generateToken(user));
        return response;
    }

    public String generateToken(User user) {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("pbhuy")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
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
        JWSVerifier verifier = new MACVerifier(SECRET);

        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier);

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        IntrospectResponse response = new IntrospectResponse();
        response.setValid(verified && expirationDate.after(new Date()));
        return response;
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        joiner.add(permission.getName());
                    });
                }
            });
        }
        return joiner.toString();
    }
}
