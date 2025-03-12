package br.com.fiap.gastrosphere.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

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

    @Schema(description = "ID do dono do restaurante.")
    @NotNull(message = "ID do dono do restaurante não pode ser nulo.")
    private UserBodyRequest user;

    @Schema(description = "Endereço do usuário.")
    @NotNull(message = "Endereço do usuário não pode ser nulo.")
    private AddressBodyRequest address;

    @Schema(description = "Tipo do restaurante.")
    @NotNull(message = "Tipo do restaurante não pode ser nulo.")
    private RestaurantTypeBodyRequest restaurantType;

    @Schema(description = "Horário de abertura.")
    @NotNull(message = "Horário de abertura não pode ser nulo.")
    @NotBlank(message = "Horário de abertura não pode ser vazio.")
    private LocalTime startedAt;

    @Schema(description = "Horário de fechamento.")
    @NotNull(message = "Horário de abertura não pode ser nulo.")
    @NotBlank(message = "Horário de abertura não pode ser vazio.")
    private LocalTime finishedAt;

}
