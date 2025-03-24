package br.com.fiap.gastrosphere.helper;

import static java.math.BigDecimal.valueOf;
import static java.util.List.of;
import static java.util.UUID.randomUUID;

import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;

public abstract class MenuHelper {
	
	public static MenuModel gerarMenu() {
		var item = MenuItemModel.builder()
				.id(randomUUID())
				.image("TESTE.PNG")
				.isAvailable(true)
				.price(valueOf(10.00))
				.build();
		return MenuModel.builder()
				.name("Teste")
				.itemsMenu(of(item))
				.build();
	}
}
