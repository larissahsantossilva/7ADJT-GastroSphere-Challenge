package br.com.fiap.gastrosphere.core.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemResponse {
    
	private UUID id;
	private UUID menuId;
    private String description;
    private String image;
    private BigDecimal price;
    private Boolean isAvailable;

    public MenuItemResponse(MenuItemModel menuItem) {
        this.id = menuItem.getId();
        this.description = menuItem.getDescription();
        this.image = menuItem.getImage();
        this.price = menuItem.getPrice();
        this.menuId = menuItem.getMenu().getId();
        this.isAvailable = menuItem.getIsAvailable();
    }
}
