package br.com.guilherme.gestao_vagas.modules.company.useCases;

import br.com.guilherme.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.guilherme.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.guilherme.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.guilherme.gestao_vagas.modules.company.repositories.CompanyRepository;
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
public class AuthCompanyUseCase {

    /*
    * A notação @Value torna possivel pergar um valor declarado no application.properties

    ! neste contexto foi usado uma palavra direto no application mas o cooreto é criar
    ! variáveis de ambiente tornando assim mais dificil de encontrar a palavra secreta
    */
    @Value("${security.token.secret.company}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException{

        /*
          *  Verifica se o Username da empresa existe no banco de dados,
          *  caso não exista ele lança uma Exceção dizendo não con encontrou o Usuário
        */
        CompanyEntity company = companyRepository.findByUsername(authCompanyDTO.getUsername())
                .orElseThrow(() ->  new UsernameNotFoundException("Usuário ou senha incorreto"));

        /*
          * Verifica se a senha informada é igual a que está gravado no banco de dados
          ? se não for igual a senha que está no banco de dados retorna um erro
          ? se for igual gera um token para o usuário fazer as requisições
        */
        boolean passwordMatches = passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        /*
          * Se a senha retornada do passwordEncoder.matches for um valor false
          * a API retorna uma Exceção informando que a senha está  incorreta
        */
        if (!passwordMatches){
            throw new AuthenticationException("Usuário ou senha incorreto");
        }

        /*
         * Se a Senha corresponder ao que está gravado no banco de dados
         * a API vai gerar o token

         ! withIssuer -> é referente a quem vai emitir o token, ou seja, a empresa ou aplicação que está criando o token.
         ! Aqui você deve especificar um identificador único para sua aplicação. É importante que esse valor seja único.

         ! withSubject -> identifica de quem é o token. Neste caso, estamos utilizando o ID da empresa.

         ! Algorithm -> Define o algoritmo de assinatura utilizado para assinar o token. Neste exemplo, esta sendo
         ! utilizando o algoritmo HMAC com chave secreta (`HMAC256(secretKey)`).

         ! sign(algorithm) -> Finaliza a construção do token e aplica a assinatura definida pelo algoritmo especificado.
         ! Este método retorna o token JWT assinado, pronto para ser usado pela sua aplicação para autenticação e autorização.

         ! withExpiresAt -> determina o tempo de duração com token, no caso foi defini do que ele vai durar 2 horas
         ! após dua criação.
        */

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant expiresIn = Instant.now().plus(Duration.ofHours(2));

        String token = JWT.create()
                .withIssuer("Minha_Aplicacao_Empresa")
                .withSubject(company.getId().toString())
                .withExpiresAt(expiresIn)
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);

        return AuthCompanyResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();
    }
}
