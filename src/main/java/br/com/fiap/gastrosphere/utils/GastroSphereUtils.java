package br.com.fiap.gastrosphere.utils;

import br.com.fiap.gastrosphere.dtos.requests.AddressBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.UserBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ID_INVALIDO;
import static java.util.regex.Pattern.matches;

public class GastroSphereUtils {
    private static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    public static void uuidValidator(UUID id) {
        if (!matches(REGEX_UUID, id.toString())) {
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
        Restaurant restaurant = new Restaurant();

        restaurant.setName(restaurantRequest.getName());
        restaurant.setAddressId(restaurantRequest.getAddressId());
        restaurant.setOwnerId(restaurantRequest.getOwnerId());
        restaurant.setAddressNumber(restaurantRequest.getAddressNumber());
        restaurant.setAddressComplement(restaurantRequest.getAddressComplement());
        restaurant.setRestaurantType(restaurantRequest.getRestaurantType());
        restaurant.setOpeningHours(restaurantRequest.getOpeningHours());

        return restaurant;
    }

}
