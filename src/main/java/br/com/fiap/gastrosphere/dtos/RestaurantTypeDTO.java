package br.com.fiap.gastrosphere.dtos;

import br.com.fiap.gastrosphere.entities.RestaurantType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantTypeDTO {

    private String name;

    public RestaurantTypeDTO(RestaurantType restaurantType){
        this.name = restaurantType.getName();
    }

}
