package br.com.fiap.gastrosphere.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Schema(description = "Nome do usuário.")
    @NotNull(message = "Nome do usuário não pode ser nulo.")
    @NotBlank(message = "Nome do usuário não pode ser vazio.")
    @Size(min = 3, max = 255)
    private String name;

    @Schema(description = "Email do usuário.")
    @NotNull(message = "Email do usuário não pode ser nulo.")
    @NotBlank(message = "Email do usuário não pode ser vazio.")
    @Size(min = 5, max = 255)
    private String email;

    @Schema(description = "Login do usuário.")
    @NotNull(message = "Login do usuário não pode ser nulo.")
    @NotBlank(message = "Login do usuário não pode ser vazio.")
    @Size(min = 3, max = 100)
    private String login;

    @Schema(description = "Senha do usuário.")
    @NotNull(message = "Senha do usuário não pode ser nula.")
    @NotBlank(message = "Senha do usuário não pode ser vazia.")
    @Size(min = 8, max = 255)
    private String password;

    @Schema(description = "Tipo de usuário.")
    @NotNull(message = "Tipo de usuário não pode ser nulo.")
    @NotBlank(message = "Tipo de usuário não pode ser vazio.")
    @Size(min = 4, max = 50)
    private String userType;

    @Schema(description = "Documento do usuário.")
    @NotNull(message = "Documento do usuário não pode ser nulo.")
    @NotBlank(message = "Documento do usuário não pode ser vazio.")
    @Size(min = 5, max = 20)
    private String document;

    @Schema(description = "ID do endereço do usuário.")
    @NotNull(message = "ID do endereço do usuário não pode ser nulo.")
    private UUID addressId;

    @Schema(description = "Número do endereço do usuário.")
    @NotNull(message = "Número do endereço do usuário não pode ser nulo.")
    @NotBlank(message = "Número do endereço do usuário não pode ser vazio.")
    @Size(min = 1, max = 10)
    private String addressNumber;

    @Schema(description = "Complemento do endereço do usuário.")
    @Size(max = 255)
    private String addressComplement;
}
