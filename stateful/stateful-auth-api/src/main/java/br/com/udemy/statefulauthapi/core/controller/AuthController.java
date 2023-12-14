package br.com.udemy.statefulauthapi.core.controller;

import br.com.udemy.statefulauthapi.core.dto.AuthRequest;
import br.com.udemy.statefulauthapi.core.dto.AuthUserResponse;
import br.com.udemy.statefulauthapi.core.dto.TokenDto;
import br.com.udemy.statefulauthapi.core.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokenDto login(@RequestBody AuthRequest request){
        return authService.login(request);
    }

    @PostMapping("token/validate")
    public TokenDto validateToken(@RequestHeader String accessToken){
        return authService.validateToken(accessToken);
    }

    @PostMapping("logout")
    public HashMap<String, Object> logout(@RequestHeader String accessToken){
        authService.logout(accessToken);
        var response = new HashMap<String, Object>();
        response.put("status", "OK");
        response.put("status", HttpStatus.OK);
        return response;
    }

    @GetMapping("user")
    public AuthUserResponse getAuthenticatedUser(@RequestHeader String accessToken) {
        return authService.getAuthenticatedUser(accessToken);
    }

}
