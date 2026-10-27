package br.com.jzbreno.security;

import br.com.jzbreno.Exceptions.InvalidJwtAutheticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class JwtTokenProvider {
    @Value( "${security.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value( "${security.token.expire-length:3600000}")
    private long validityInMilliSeconds = 3600000;

    private UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
        String accessToken = getAccessToken(username, roles, now, validaty);
        String refreshToken = getRefreshToken(username, roles,now);
        return new TokenDTO(username, true, now, validaty, accessToken, refreshToken);
    }

    private String getAccessToken(String username,List<String> roles, Date now, Date validity) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return JWT.create() //cria o token
                .withClaim("roles", roles)//adiciona as permissoes do usuario
                .withIssuedAt(now) //data de criacao do token
                .withExpiresAt(validity)//data de expiracao do token
                .withSubject(username)//usuario que esta logado
                .withIssuer(issueUrl)//url de onde o token foi gerado
                .sign(algorithm)//assina o token
                .toString();///retorna o token


    }

    private String getRefreshToken(String username, List<String> roles, Date now) {

        Date refreshValidity = new Date(now.getTime() + validityInMilliSeconds * 3);

        return JWT.create() //cria o token
                .withClaim("roles", roles)//adiciona as permissoes do usuario
                .withIssuedAt(now) //data de criacao do token
                .withExpiresAt(refreshValidity)//data de expiracao do token
                .withSubject(username)//usuario que esta logado
                .sign(algorithm)//assina o token
                .toString();///retorna o token
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedJwt = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJwt.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if(decodedJWT.getExpiresAt().before(new Date())){
                return false;
            }
            return true;
        }catch (Exception e){
            throw new InvalidJwtAutheticationException("Expired or invalid JWT token");
        }
    }

}
