package com.pbhuy.identityservice.configs;

import com.nimbusds.jose.JOSEException;
import com.pbhuy.identityservice.dto.request.IntrospectRequest;
import com.pbhuy.identityservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class JwtTokenDecoder implements JwtDecoder {

    private final AuthService authService;

    @Value("${jwt.secret-key}")
    protected String SECRET;

    @Autowired
    public JwtTokenDecoder(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            authService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e.getMessage());
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        return nimbusJwtDecoder.decode(token);
    }
}
