package br.com.udemy.statelessauthapi.core.controller;

import br.com.udemy.statelessauthapi.core.dto.AuthRequest;
import br.com.udemy.statelessauthapi.core.dto.TokenDto;
import br.com.udemy.statelessauthapi.core.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokenDto login(@RequestBody AuthRequest request){
        return authService.login(request);
    }

    @PostMapping("token/validate")
    public TokenDto validate(@RequestHeader String accessToken){
        return authService.validateToken(accessToken);
    }
}
