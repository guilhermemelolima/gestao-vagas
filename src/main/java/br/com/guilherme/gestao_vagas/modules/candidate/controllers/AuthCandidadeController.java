package br.com.guilherme.gestao_vagas.modules.candidate.controllers;

import br.com.guilherme.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.guilherme.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.guilherme.gestao_vagas.modules.candidate.useCases.AuthCandidadeUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidate")
public class AuthCandidadeController {

    @Autowired
    private AuthCandidadeUseCase authCandidadeUseCase;

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody AuthCandidateRequestDTO authCandidateRequestDTO){

        try{

            AuthCandidateResponseDTO token = authCandidadeUseCase.execute(authCandidateRequestDTO);
            return new ResponseEntity<>(token, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }

    }
}
