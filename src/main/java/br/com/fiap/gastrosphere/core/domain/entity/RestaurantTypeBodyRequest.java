package br.com.fiap.gastrosphere.core.domain.entity;

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
public class RestaurantTypeBodyRequest {
    @Schema(description = "Tipo do restaurante.")
    @NotNull(message = "Tipo do restaurante não pode ser nulo.")
    @NotBlank(message = "Tipo do restaurante não pode ser vazio.")
    @Size(min = 3, max = 100)
    private String name;
}
