package br.com.fiap.gastrosphere.core.application.dto.request;

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
public class AddressBodyRequest {

    @Schema(description = "País do endereço.")
    @NotNull(message = "País do endereço não pode ser nulo.")
    @NotBlank(message = "País do endereço não pode ser vazio.")
    @Size(min = 3, max = 100)
    private String country;

    @Schema(description = "Estado do endereço.")
    @NotNull(message = "Estado do endereço não pode ser nulo.")
    @NotBlank(message = "Estado do endereço não pode ser vazio.")
    @Size(min = 3, max = 100)
    private String state;

    @Schema(description = "Cidade do endereço.")
    @NotNull(message = "Cidade do endereço não pode ser nulo.")
    @NotBlank(message = "Cidade do endereço não pode ser vazio.")
    @Size(min = 3, max = 100)
    private String city;

    @Schema(description = "CEP do endereço.")
    @NotNull(message = "CEP do endereço não pode ser nulo.")
    @NotBlank(message = "CEP do endereço não pode ser vazio.")
    @Size(min = 9, max = 9)
    private String zipCode;

    @Schema(description = "Rua do endereço.")
    @NotNull(message = "Rua do endereço não pode ser nulo.")
    @NotBlank(message = "Rua do endereço não pode ser vazio.")
    @Size(min = 1, max = 255)
    private String street;

    @Schema(description = "Número do endereço.")
    @NotNull(message = "Número do endereço não pode ser nulo.")
    @NotBlank(message = "Número do endereço não pode ser vazio.")
    @Size(min = 1, max = 10)
    private String number;

    @Schema(description = "Complemento do endereço do usuário.")
    @Size(max = 255)
    private String complement;

}
