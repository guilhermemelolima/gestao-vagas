package br.com.guilherme.gestao_vagas.modules.company.controllers;

import br.com.guilherme.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.guilherme.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.guilherme.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class AuthCompanyController {

    @Value("${security.token.secret.company}")
    private String secretKey;

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;

    @PostMapping("/auth")
    public ResponseEntity<Object> create(@RequestBody AuthCompanyDTO authCompanyDTO) {
        try{
            AuthCompanyResponseDTO token = authCompanyUseCase.execute(authCompanyDTO);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
}
