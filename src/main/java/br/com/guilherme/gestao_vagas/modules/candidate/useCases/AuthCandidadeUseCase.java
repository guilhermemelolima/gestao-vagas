package br.com.guilherme.gestao_vagas.modules.candidate.useCases;

import br.com.guilherme.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.guilherme.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.guilherme.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCandidadeUseCase {

    @Value("${security.token.secret.candidade}")
    private String secretKey;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {

        CandidateEntity candidate= candidateRepository.findByUsername(authCandidateRequestDTO.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha incorreto"));

        boolean passwordMatches = passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if (!passwordMatches){
            throw new AuthenticationException();
        }

        /*
         ! withClaim -> adiciona um role/papel/categoria/nivel ao usuário,
         ! no caso é adicionada uma lista de possibilidades, neste exemplo é adicionado um unico role
        */
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        String token = JWT.create()
                .withIssuer("Minha_Aplicacao_Empresa")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", Arrays.asList("CANDIDATE"))
                .withExpiresAt(expiresIn)
                //.withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
                .sign(algorithm);

        return AuthCandidateResponseDTO.builder()
                .access_token(token)
                 .expires_in(expiresIn.toEpochMilli())
                .build();
    }
}
