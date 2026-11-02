package br.com.jzbreno.services;

import br.com.jzbreno.repository.UserPermissionRepository;
import br.com.jzbreno.security.AccountCredentialsDTO;
import br.com.jzbreno.security.JwtTokenProvider;
import br.com.jzbreno.security.TokenDTO;
import com.fasterxml.jackson.databind.deser.std.TokenBufferDeserializer;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserPermissionRepository repository;

    public ResponseEntity<TokenDTO> singIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername());

        if(user == null) throw new UsernameNotFoundException("User not found: " + credentials.getUsername());
        //esta inserindo o .get().getUserName() por que o respository retorna um Optional<>, o primeiro .get() extrai o retorno encapsulado
        var tokenResponse = jwtTokenProvider.createToken(user.get().getUsername(), user.get().getRoles());


        return ResponseEntity.ok(tokenResponse);
    }


}
