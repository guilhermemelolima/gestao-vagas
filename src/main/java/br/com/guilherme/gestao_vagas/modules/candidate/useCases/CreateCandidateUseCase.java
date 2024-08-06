package br.com.guilherme.gestao_vagas.modules.candidate.useCases;

import br.com.guilherme.gestao_vagas.exceptions.UserFoundException;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateEntity candidateEntity){

        candidateRepository
                .findByUsername(candidateEntity.getUsername())
                .ifPresent((user) -> {
                    throw new UserFoundException("Username " + user.getUsername() + " já cadastrado");
                });

        candidateRepository
                .findByEmail(candidateEntity.getEmail())
                .ifPresent((user) -> {
                    throw new UserFoundException("Email " + user.getEmail() + " já cadastrado");
                });

        String password = passwordEncoder.encode(candidateEntity.getPassword());

        candidateEntity.setPassword(password);

        return candidateRepository.save(candidateEntity);
    }

}
