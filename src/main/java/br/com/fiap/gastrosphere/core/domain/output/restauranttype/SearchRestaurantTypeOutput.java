package br.com.fiap.gastrosphere.core.domain.output.restauranttype;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputStatus;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchRestaurantTypeOutput implements OutputInterface {

    private List<RestaurantType> restaurantTypesEntity = new ArrayList<>();
    private OutputStatus outputStatus;

    public SearchRestaurantTypeOutput(List<RestaurantType> restaurantTypesEntity, OutputStatus outputStatus) {
        this.restaurantTypesEntity = restaurantTypesEntity;
        this.outputStatus = outputStatus;
    }

    @Override
    public Object getBody() {
        return this.restaurantTypesEntity;
    }
}
