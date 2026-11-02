package br.com.jzbreno.controllers;

import br.com.jzbreno.security.AccountCredentialsDTO;
import br.com.jzbreno.security.TokenDTO;
import br.com.jzbreno.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Enpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping("/signin")
    @Operation(summary = "Authenticates an user and retornes a token")
    public ResponseEntity<TokenDTO> signIn( @RequestBody AccountCredentialsDTO credentials) {
        if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var token = authService.singIn(credentials);
        if(token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok().body(token.getBody());
    }

}
