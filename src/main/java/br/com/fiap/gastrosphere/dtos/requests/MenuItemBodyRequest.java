package br.com.fiap.gastrosphere.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class MenuItemBodyRequest {
	
    @Schema(description = "ID do menu.")
    @NotNull(message = "ID do menu não pode ser nulo.")
    private UUID menuId;

    @Schema(description = "Descrição do Item.")
    @NotNull(message = "A descrição do Item não pode ser nulo.")
    @NotBlank(message = "A descrição do Item não pode ser vazio.")
    @Size(min = 1, max = 255)
	private String description;
	
    @Schema(description = "Preço do Item.")
    @Positive(message = "O Preço deve ser positivo")
    @DecimalMin(value = "0.01", message = "O Preço não pode ser menor que 0.01")
    @DecimalMax(value = "1000.00", message = "O Preço não pode ser maior que 1000.00")
    @NotNull(message = "O Preço do Item não pode ser nulo.")
	private BigDecimal price;
	
    @Schema(description = "Disponibilidade do Item.")
    @NotNull(message = "A Disponibilidade do Item não pode ser nulo.")
	private Boolean isAvailable;
	
	@Schema(description = "Imagem do Item.")
	@NotNull(message = "A Imagem do Item não pode ser nulo.")
	@NotBlank(message = "A Imagem do Item não pode ser vazio.")
	@Size(min = 1, max = 255)
	private String image;
	
	@Schema(description = "Data de criação.")
    @NotNull(message = "Data de criação não pode ser nulo.")
	private LocalDate createdAt;
	
	@Schema(description = "Data de edição.")
	@NotNull(message = "Data de edição não pode ser nulo.")
	private LocalDate lastMododified;
}
