package br.com.fiap.gastrosphere.core.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {

    @Schema(description = "Antiga senha do usuário.")
    @NotNull(message = "Antiga senha não pode ser nula.")
    @NotBlank(message = "Antiga senha não pode ser vazia.")
    private String oldPassword;

    @Schema(description = "Nova senha do usuário.")
    @NotNull(message = "Nova senha não pode ser nula.")
    @NotBlank(message = "Nova senha não pode ser vazia.")
    private String newPassword;
}
