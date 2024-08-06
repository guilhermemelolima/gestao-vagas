package br.com.guilherme.gestao_vagas.modules.candidate.useCases;

import br.com.guilherme.gestao_vagas.exceptions.JobNotFoundException;
import br.com.guilherme.gestao_vagas.exceptions.UserNotFoundException;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.guilherme.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.guilherme.gestao_vagas.modules.company.entities.JobEntity;
import br.com.guilherme.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplyJobCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    //  Recebe ID do Candidadto
    //  Recebe ID da vaga
    public ApplyJobEntity execete(UUID idCandidate, UUID idJob){

        // Validar se o candidato exite
        candidateRepository.findById(idCandidate).orElseThrow(UserNotFoundException::new);

        //validar que a vaga existe
        jobRepository.findById(idJob).orElseThrow(JobNotFoundException::new);

        //candidato se inscrever na vaga
        ApplyJobEntity applyJob = ApplyJobEntity.builder()
                .candidateId(idCandidate)
                .jobId(idJob).build();

        return applyJobRepository.save(applyJob);

    }
}
