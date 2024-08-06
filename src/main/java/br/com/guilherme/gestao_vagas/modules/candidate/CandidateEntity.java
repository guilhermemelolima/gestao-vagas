package br.com.guilherme.gestao_vagas.modules.candidate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "candidate")
@Data
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(example = "Guilherme Melo")
    private String name;

    // expressão regular que não permite espaço
    @NotBlank()
    @Pattern(regexp = "\\S+", message = "O campo [username] não deve conter espaço")
    @Schema(
            example = "gui.melo",
            description = "Username do candidado"
    )
    private String username;

    @Email(message = "O campo [email] deve conter um e-mail válido")
    @Schema(
            example = "gui@mail.com",
            description = "Email do candidado"
    )
    private String email;

    @Length(min = 10, max = 100, message = "O campo [senha] deve conter de 10 a 100 caracteres")
    @Schema(
            example = "0123456789",
            minLength = 10, maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Senha do candidado"
    )
    private String password;

    @Schema(
            example = "Desenvolvedor Java",
            description = "Breve descrição do candidado"
    )
    private String description;
    private String curriculum;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
