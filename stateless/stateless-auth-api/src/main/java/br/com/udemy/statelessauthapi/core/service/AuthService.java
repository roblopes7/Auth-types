package br.com.udemy.statelessauthapi.core.service;

import br.com.udemy.statelessauthapi.core.dto.AuthRequest;
import br.com.udemy.statelessauthapi.core.dto.TokenDto;
import br.com.udemy.statelessauthapi.core.repository.UserRepository;
import br.com.udemy.statelessauthapi.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenDto login(AuthRequest request){
        var user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() -> new ValidationException("User not found!"));

        var accessToken = jwtService.createToken(user);
        validatePassword(request.password(), user.getPassword());
        return new TokenDto(accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if(ObjectUtils.isEmpty(rawPassword)){
            throw new ValidationException("The Password must be informed!");
        }
        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new ValidationException("Password is incorrect!");
        }
    }

    public TokenDto validateToken(String accessToken) {
        validateExistingToken(accessToken);
        jwtService.validateAccessToken(accessToken);
        return new TokenDto(accessToken);
    }

    private void validateExistingToken(String accessToken){
        if(ObjectUtils.isEmpty(accessToken)){
            throw new ValidationException("The access token must be informed!");
        }
    }
}
