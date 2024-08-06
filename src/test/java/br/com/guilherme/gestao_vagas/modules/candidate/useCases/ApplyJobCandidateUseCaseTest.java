package br.com.guilherme.gestao_vagas.modules.candidate.useCases;

import br.com.guilherme.gestao_vagas.exceptions.JobNotFoundException;
import br.com.guilherme.gestao_vagas.exceptions.UserNotFoundException;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.guilherme.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.guilherme.gestao_vagas.modules.company.entities.JobEntity;
import br.com.guilherme.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyJobCandidateUseCaseTest {

    @InjectMocks
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @Test
    @DisplayName("Should not be able to apply job with candidate not found")
    void should_not_be_able_to_apply_job_with_candidate_not_found(){
        try{
            applyJobCandidateUseCase.execete(null, null);
        }catch(Exception e){
            assertThat(e).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Should not be able to apply job with job not found")
    void should_not_be_able_to_apply_job_with_job_not_found(){
        UUID idCandidate = UUID.randomUUID();

        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(idCandidate);

        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));

        try {
            applyJobCandidateUseCase.execete(idCandidate, null);
        }catch (Exception e){
            assertThat(e).isInstanceOf(JobNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Should not be able to create a new apply job ")
    void should_not_be_able_to_create_a_new_apply_job() {
        UUID idCandidate = UUID.randomUUID();
        UUID idJob = UUID.randomUUID();

        ApplyJobEntity applyJob = ApplyJobEntity.builder().candidateId(idCandidate).jobId(idJob).build();

        ApplyJobEntity applyJobCreated = ApplyJobEntity.builder().id(UUID.randomUUID()).build();

        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(new CandidateEntity()));
        when(jobRepository.findById(idJob)).thenReturn(Optional.of(new JobEntity()));

        when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

        ApplyJobEntity result = applyJobCandidateUseCase.execete(idCandidate, idJob);

        assertThat(result).hasFieldOrProperty("id");
        assertNotNull(result.getId());
    }
}
