package br.com.guilherme.gestao_vagas.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTCompanyProvider {

    @Value("${security.token.secret.company}")
    private String secretKeyCompany;

    public DecodedJWT validateToken(String token){

        token = token.replace("Bearer ", "");

        Algorithm algorithm = Algorithm.HMAC256(secretKeyCompany);

        try{
            return JWT.require(algorithm)
                    .build()
                    .verify(token);

        }catch (JWTVerificationException e){
            return null;
        }
    }
}
