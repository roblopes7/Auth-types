package br.com.udemy.statefulauthapi.core.service;

import br.com.udemy.statefulauthapi.core.dto.AuthRequest;
import br.com.udemy.statefulauthapi.core.dto.AuthUserResponse;
import br.com.udemy.statefulauthapi.core.dto.TokenDto;
import br.com.udemy.statefulauthapi.core.model.User;
import br.com.udemy.statefulauthapi.core.repository.UserRepository;
import br.com.udemy.statefulauthapi.infra.exception.AuthenticationException;
import br.com.udemy.statefulauthapi.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public TokenDto login(AuthRequest request) {
        var user = findByUsername(request.username());
        var accessToken = tokenService.createTokenn(user.getUsername());
        validatePassword(request.password(), user.getPassword());
        return new TokenDto(accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword){
        if(ObjectUtils.isEmpty(rawPassword)){
            throw new ValidationException("The Password must be informed!");
        }
        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new ValidationException("Password is incorrect!");
        }
    }

    public TokenDto validateToken(String accessToken){
        validateExistingToken(accessToken);
        var valid = tokenService.validateAccessToken(accessToken);
        if(valid){
            return new TokenDto(accessToken);
        }
        throw new AuthenticationException("Invalid token!");
    }

    public AuthUserResponse getAuthenticatedUser(String accessToken){
        var tokenData = tokenService.getTokenData(accessToken);
        var user = findByUsername(tokenData.username());
        return new AuthUserResponse(user.getId(), user.getUsername());
    }

    public void logout(String accessToken){
        tokenService.deleteRedisToken(accessToken);
    }

    private User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new ValidationException("User not found!"));
    }

    private void validateExistingToken(String accessToken) {
        if(ObjectUtils.isEmpty(accessToken)){
            throw new ValidationException("The access token must be informed!");
        }
    }
}
