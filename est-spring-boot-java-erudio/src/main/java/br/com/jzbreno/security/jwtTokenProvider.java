package br.com.jzbreno.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class jwtTokenProvider {
    @Value( "${security.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value( "${security.token.expire-length:3600000}")
    private long validityInMilliSeconds = 3600000;

    private UserDetailsService userDetailsService;

    public jwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        //assim ele funcionava?
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey);
    }

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    //esse cara roda apos a injecao de dependencias, pois no uso do @autowired quando ele roda, as variaveis ainda nao foram inicializadas
    //solucao = usar o @postconstruct ou injecao de dependencias no construtor
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public TokenDTO createToken(String username, List<String> roles) {
        Date now = new Date();
        Date validaty = new Date(now.getTime() + validityInMilliSeconds);
        String accessToken = getAccessToken(username, true, now, validaty);
        String refreshToken = getRefreshToken(username, roles,now,validaty);
        return new TokenDTO(username, true, now, validaty, accessToken, refreshToken);
    }

    //todo: criar metodo para gerar o token
    private String getAccessToken(String username, boolean b, Date now, Date validaty) {
        return "";
    }

    private String getRefreshToken(String username, List<String> roles, Date now, Date validity) {
        return "";
    }

}
