package br.com.udemy.statefulanyapi.core.service;

import br.com.udemy.statefulanyapi.core.client.TokenClient;
import br.com.udemy.statefulanyapi.core.dto.AuthUserResponse;
import br.com.udemy.statefulanyapi.infra.exception.AuthenticationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
@Slf4j
public class TokenService {

    private final TokenClient tokenClient;

    public void validateToken(String token) {
        try {
            log.info("Sending request for token validation {}", token);
            var response = tokenClient.validateToken(token);
            log.info("Token is valid: {}", response.accessToken());
        } catch (Exception e) {
            throw new AuthenticationException("Auth error: " + e.getMessage());
        }
    }

    public AuthUserResponse getAuthenticatedUser(String token) {
        try {
            log.info("Sending request for auth user: {}", token);
            var response = tokenClient.getAuthenticatedUser(token);
            log.info("Auth user found: {}", response.toString());
            return response;
        } catch (Exception e) {
            throw new AuthenticationException("Auth to get authenticated user: " + e.getMessage());
        }
    }
}
