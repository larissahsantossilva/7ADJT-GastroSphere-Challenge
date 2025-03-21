package br.com.fiap.gastrosphere.utils;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ID_INVALIDO;

import java.util.UUID;
import java.util.regex.Pattern;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.entity.RestaurantTypeBodyRequest;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import org.modelmapper.ModelMapper;

import br.com.fiap.gastrosphere.core.application.dto.request.AddressBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.MenuBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.request.MenuItemBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.request.UserBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.request.UserTypeBodyRequest;
import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.Menu;
import br.com.fiap.gastrosphere.core.infra.model.MenuItem;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;

public final class GastroSphereUtils {
	
	private GastroSphereUtils() {}
	
    private static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    public static void uuidValidator(UUID id) {
        if (!Pattern.matches(REGEX_UUID, id.toString())) {
            throw new ResourceNotFoundException(ID_INVALIDO);
        }
    }

    public static AddressModel convertToAddress(AddressBodyRequest addressDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDto, AddressModel.class);
    }

    public static UserModel convertToUser(UserBodyRequest userRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userRequest, UserModel.class);
    }

    public static Restaurant convertToRestaurant(RestaurantBodyRequest restaurantRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(restaurantRequest, Restaurant.class);
    }

    public static UserTypeModel convertToUserType(UserTypeBodyRequest userTypeDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userTypeDto, UserTypeModel.class);
    }

    public static RestaurantTypeModel convertToRestaurantType(RestaurantTypeBodyRequest restaurantTypeBodyResquest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(restaurantTypeBodyResquest, RestaurantTypeModel.class);
    }
    
    public static MenuItem convertToMenuItem(MenuItemBodyRequest menuItemRequest) {
    	ModelMapper modelMapper = new ModelMapper();
    	return modelMapper.map(menuItemRequest, MenuItem.class);
    }

    public static Menu convertToMenu(MenuBodyRequest menuRequest) {
    	Menu menu = new Menu();
        menu.setRestaurantId(menuRequest.getRestaurantId());
        menu.setName(menuRequest.getName());
        if (menuRequest.getItemsMenu() != null) {
            menu.setItemsMenu(
                menuRequest.getItemsMenu().stream()
                    .map(itemRequest -> {
                        MenuItem menuItem = new MenuItem();
                        menuItem.setDescription(itemRequest.getDescription());
                        menuItem.setPrice(itemRequest.getPrice());
                        menuItem.setIsAvailable(itemRequest.getIsAvailable());
                        menuItem.setImage(itemRequest.getImage());
                        menuItem.setMenu(menu);
                        if(itemRequest.getId() != null) {
                        	menuItem.setId(itemRequest.getId());
                        }
                        return menuItem;
                    })
                    .toList()
            );
        }
    	return menu;
    }
    public static RestaurantType convertToRestaurantType(RestaurantTypeModel model) {
        RestaurantType restaurantType = new RestaurantType();
        restaurantType.setId(model.getId());
        restaurantType.setName(model.getName());
        restaurantType.setCreatedAt(model.getCreatedAt());
        restaurantType.setLastModifiedAt(model.getLastModifiedAt());
        return restaurantType;
    }
}
