package br.com.fiap.gastrosphere.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyRequest {
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
    private UserTypeBodyRequest userType;

    @Schema(description = "Documento do usuário.")
    @NotNull(message = "Documento do usuário não pode ser nulo.")
    @NotBlank(message = "Documento do usuário não pode ser vazio.")
    @Size(min = 5, max = 20)
    private String document;

    @Schema(description = "Endereço do usuário.")
    @NotNull(message = "Endereço do usuário não pode ser nulo.")
    private AddressBodyRequest address;

}
