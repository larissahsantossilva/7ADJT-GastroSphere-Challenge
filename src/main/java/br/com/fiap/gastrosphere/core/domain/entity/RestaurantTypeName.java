package br.com.fiap.gastrosphere.core.domain.entity;

import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantTypeName {

    private String name;

    public RestaurantTypeName(RestaurantTypeModel restaurantType){
        this.name = restaurantType.getName();
    }

}
