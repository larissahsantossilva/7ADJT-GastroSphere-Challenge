package br.com.fiap.gastrosphere.core.application.dto.response;

import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.com.fiap.gastrosphere.core.infra.model.MenuModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuBodyResponse {
	
    private UUID id;
    private UUID restaurantId;
    private String name;
    private LocalDate createdAt = now();
    private LocalDate lastModifiedAt = now();
    private List<MenuItemResponse> itemsMenu;

	public MenuBodyResponse(MenuModel menu) {
		this.id = menu.getId();
		this.restaurantId = menu.getRestaurantId();
		this.name = menu.getName();
		this.createdAt = menu.getCreatedAt();
		this.lastModifiedAt = menu.getLastModifiedAt();
		this.itemsMenu = menu.getItemsMenu().stream().map(MenuItemResponse::new).toList();
	}
}
