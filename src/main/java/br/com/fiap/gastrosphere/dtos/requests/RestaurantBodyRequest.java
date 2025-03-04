package br.com.fiap.gastrosphere.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBodyRequest {
    @Schema(description = "Nome do restaurante.")
    @NotNull(message = "Nome do restaurante não pode ser nulo.")
    @NotBlank(message = "Nome do restaurante não pode ser vazio.")
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "ID do endereço do restaurante.")
    @NotNull(message = "ID do endereço do restaurante não pode ser nulo.")
    private UUID addressId;

    @Schema(description = "Número do endereço do restaurante.")
    @NotNull(message = "Número do endereço do restaurante não pode ser nulo.")
    @NotBlank(message = "Número do endereço do restaurante não pode ser vazio.")
    @Size(min = 1, max = 10)
    private String addressNumber;

    @Schema(description = "Complemento do endereço do restaurante.")
    @Size(max = 255)
    private String addressComplement;

    @Schema(description = "Tipo do restaurante.")
    @NotNull(message = "Tipo do restaurante não pode ser nulo.")
    @NotBlank(message = "Tipo do restaurante não pode ser vazio.")
    @Size(min = 1, max = 100)
    private String restaurantType;

    @Schema(description = "Horário de funcionamento do restaurante.")
    @Size(max = 100)
    private String openingHours;

    @Schema(description = "ID do dono do restaurante.")
    @NotNull(message = "ID do dono do restaurante não pode ser nulo.")
    private UUID ownerId;
}
