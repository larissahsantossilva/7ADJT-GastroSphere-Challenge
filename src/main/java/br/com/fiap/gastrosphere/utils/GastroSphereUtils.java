package br.com.fiap.gastrosphere.utils;

import br.com.fiap.gastrosphere.dtos.requests.*;
import br.com.fiap.gastrosphere.entities.*;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;

public final class GastroSphereUtils {
	
	private GastroSphereUtils() {}
	
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

}
