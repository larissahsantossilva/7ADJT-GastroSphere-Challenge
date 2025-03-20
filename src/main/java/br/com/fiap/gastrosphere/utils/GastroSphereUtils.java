package br.com.fiap.gastrosphere.utils;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ID_INVALIDO;

import java.util.UUID;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;

import br.com.fiap.gastrosphere.dtos.requests.AddressBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.MenuBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.MenuItemBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.RestaurantTypeBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.UserBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.UserTypeBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.Menu;
import br.com.fiap.gastrosphere.entities.MenuItem;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.entities.RestaurantType;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.entities.UserType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;

public final class GastroSphereUtils {
	
	private GastroSphereUtils() {}
	
    private static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    public static void uuidValidator(UUID id) {
        if (!Pattern.matches(REGEX_UUID, id.toString())) {
            throw new ResourceNotFoundException(ID_INVALIDO);
        }
    }

    public static Address convertToAddress(AddressBodyRequest addressDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDto, Address.class);
    }

    public static User convertToUser(UserBodyRequest userRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userRequest, User.class);
    }

    public static Restaurant convertToRestaurant(RestaurantBodyRequest restaurantRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(restaurantRequest, Restaurant.class);
    }

    public static UserType convertToUserType(UserTypeBodyRequest userTypeDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userTypeDto, UserType.class);
    }

    public static RestaurantType convertToRestaurantType(RestaurantTypeBodyRequest restaurantTypeBodyResquest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(restaurantTypeBodyResquest, RestaurantType.class);
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
}
