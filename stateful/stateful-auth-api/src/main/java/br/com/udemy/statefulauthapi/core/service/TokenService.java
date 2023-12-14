package br.com.udemy.statefulauthapi.core.service;

import br.com.udemy.statefulauthapi.core.dto.TokenData;
import br.com.udemy.statefulauthapi.infra.exception.AuthenticationException;
import br.com.udemy.statefulauthapi.infra.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Long ONE_DAY_IN_SECONDS = 86400L;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public String createTokenn(String username){
        var accessToken = UUID.randomUUID().toString();
        var data = new TokenData(username);
        var jsonData = getJsonData(data);
        redisTemplate.opsForValue().set(accessToken, jsonData);
        redisTemplate.expireAt(accessToken, Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));
        return accessToken;
    }

    private String getJsonData(Object payload){
        try{
            return objectMapper.writeValueAsString(payload);
        }catch (Exception e){
            return "";
        }
    }

    public TokenData getTokenData(String token){
        var accessToken = extractToken(token);
        var jsonString = getRedisTokenValue(accessToken);
        try {
            return objectMapper.readValue(jsonString, TokenData.class);
        } catch (Exception e) {
            throw new AuthenticationException("Error extract the authenticated user: " + e.getMessage());
        }
    }

    public boolean validateAccessToken(String token){
        var accessToken = extractToken(token);
        var data = getRedisTokenValue(accessToken);
        return !ObjectUtils.isEmpty(data);
    }

    private String getRedisTokenValue(String token){
        return redisTemplate.opsForValue().get(token);
    }


    public void deleteRedisToken(String token){
        var accessToken = extractToken(token);
        redisTemplate.delete(accessToken);
    }


    private String extractToken(String token){
        if(ObjectUtils.isEmpty(token)){
            throw new ValidationException("The access token was not informed.");
        }
        if(token.contains(EMPTY_SPACE)){
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
