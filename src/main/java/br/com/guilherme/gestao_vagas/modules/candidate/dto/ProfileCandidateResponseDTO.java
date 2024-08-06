package br.com.guilherme.gestao_vagas.modules.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCandidateResponseDTO {

    private UUID id;

    @Schema(example = "Guilherme Melo")
    private String name;

    @Schema(example = "gui.melo")
    private String username;

    @Schema(example = "gui@mail.com")
    private String email;

    @Schema(example = "Desenvolvedora Java")
    private String description;
}
