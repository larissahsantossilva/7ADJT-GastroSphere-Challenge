package br.com.fiap.gastrosphere.dtos.responses;

import br.com.fiap.gastrosphere.dtos.AddressDTO;
import br.com.fiap.gastrosphere.dtos.RestaurantTypeDTO;
import br.com.fiap.gastrosphere.dtos.UserDTO;
import br.com.fiap.gastrosphere.entities.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantBodyResponse {

    private UUID id;
    private String name;
    private UserDTO user;
    private AddressDTO address;
    private RestaurantTypeDTO restaurantType;
    private LocalTime startedAt;
    private LocalTime finishedAt;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;

    public RestaurantBodyResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.user = restaurant.getUser() != null ? new UserDTO(restaurant.getUser()) : null;
        this.address = restaurant.getAddress() != null ? new AddressDTO(restaurant.getAddress()) : null;
        this.restaurantType = restaurant.getRestaurantType() != null ? new RestaurantTypeDTO(restaurant.getRestaurantType()) : null;
        this.startedAt = restaurant.getStartedAt();
        this.finishedAt = restaurant.getFinishedAt();
        this.createdAt = restaurant.getCreatedAt();
        this.lastModifiedAt = restaurant.getLastModifiedAt();
    }

}
