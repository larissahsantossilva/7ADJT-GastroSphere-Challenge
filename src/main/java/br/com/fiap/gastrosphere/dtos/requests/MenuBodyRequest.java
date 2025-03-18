package br.com.fiap.gastrosphere.dtos.requests;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MenuBodyRequest {

    @Schema(description = "ID do restaurante.")
    @NotNull(message = "ID do restaurante não pode ser nulo.")
    private UUID restaurantId;

    @Schema(description = "Nome do Menu.")
    @NotNull(message = "Nome do Menu não pode ser nulo.")
    private String name;
	
	@Schema(description = "Data de criação.")
    @NotNull(message = "Data de criação não pode ser nulo.")
	private LocalDate createdAt;
	
	@Schema(description = "Data de edição.")
	@NotNull(message = "Data de edição não pode ser nulo.")
	private LocalDate lastMododified;
	
	@Valid
	private List<MenuItemBodyRequest> itemsMenu;
}
