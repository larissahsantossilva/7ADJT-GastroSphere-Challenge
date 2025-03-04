package br.com.fiap.gastrosphere.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginUserDTO(
    @Schema(description = "Antiga senha do usuário.")
    @NotNull(message = "Antiga senha não pode ser nula.")
    @NotBlank(message = "Antiga senha não pode ser vazia.")
    String oldPassword,

    @Schema(description = "Nova senha do usuário.")
    @NotNull(message = "Nova senha não pode ser nula.")
    @NotBlank(message = "Nova senha não pode ser vazia.")
    String newPassword) {
}
