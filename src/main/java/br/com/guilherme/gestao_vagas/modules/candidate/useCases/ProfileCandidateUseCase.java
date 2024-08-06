package br.com.guilherme.gestao_vagas.modules.candidate.useCases;

import br.com.guilherme.gestao_vagas.exceptions.UserNotFoundException;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.guilherme.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    public ProfileCandidateResponseDTO execute(UUID idCandidate){
        CandidateEntity candidade = candidateRepository.findById(idCandidate)
                .orElseThrow(UserNotFoundException::new);

        return ProfileCandidateResponseDTO.builder()
                .description(candidade.getDescription())
                .name(candidade.getName())
                .email(candidade.getEmail())
                .name(candidade.getName())
                .id(candidade.getId())
                .build();
    }

}
