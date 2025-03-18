package br.com.fiap.gastrosphere.dtos.responses;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.fiap.gastrosphere.entities.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemResponse {
    
	private UUID id;
    private String description;
    private BigDecimal price;
    private Boolean isAvailable;

    public MenuItemResponse(MenuItem menuItem) {
        this.id = menuItem.getId();
        this.description = menuItem.getDescription();
        this.price = menuItem.getPrice();
        this.isAvailable = menuItem.getIsAvailable();
    }
}
