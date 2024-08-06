package br.com.guilherme.gestao_vagas.modules.candidate.controllers;

import br.com.guilherme.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.guilherme.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.guilherme.gestao_vagas.modules.candidate.useCases.ApplyJobCandidateUseCase;
import br.com.guilherme.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.com.guilherme.gestao_vagas.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import br.com.guilherme.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import br.com.guilherme.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato",description = "Informações do candidato")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @Autowired
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    @PostMapping("/")
    @Operation(summary = "Cadatro de candidado",
            description = "Essa função é responsavel por cadastrar um dandidado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CandidateEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "User found")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            CandidateEntity candidate = createCandidateUseCase.execute(candidateEntity);
            return new ResponseEntity<>(candidate, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do candidato",
            description = "Essa função é responsável por buscar as informações do perfil do candidato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getProfile(HttpServletRequest request){

        Object idCandidate = request.getAttribute("candidate_id");

        try{
            ProfileCandidateResponseDTO profile = profileCandidateUseCase
                    .execute(UUID.fromString(idCandidate.toString()));
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    /*
        Abaixo podemos definir alguins parametros para o swagger que
        mudam a forma como vai ser visto na docuemntação
    */
    @Operation(summary = "Listagem de vagas disponível para o candidato",
            description = "Essa função é responsavel por listar todas as vagas disponíveis, baseadas no filtro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = JobEntity.class))
                    )
            })
    })
    @SecurityRequirement(name = "jwt_auth")
    public List<JobEntity> findJobByFilter(@RequestParam String filter){
        return listAllJobsByFilterUseCase.execute(filter);
    }

    @PostMapping("/job/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Inscrição do candidato para uma vaga",
            description = "Essa função é responsável por ralizar a inscrição do candidato em uma vaga.")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID idJob){
        Object idCandidate = request.getAttribute("candidate_id");

        try{
            ApplyJobEntity result = applyJobCandidateUseCase.execete(UUID.fromString(idCandidate.toString()),idJob);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
