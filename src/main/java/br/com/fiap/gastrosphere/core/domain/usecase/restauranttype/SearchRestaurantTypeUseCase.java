package br.com.fiap.gastrosphere.core.domain.usecase.restauranttype;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.SearchRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputStatus;
import br.com.fiap.gastrosphere.core.domain.output.restauranttype.SearchRestaurantTypeOutput;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchRestaurantTypeUseCase {
    private final SearchRestaurantTypeInterface searchRestaurant;
    private OutputInterface searchRestaurantOutput;

    public List<RestaurantType> execute(int page, int size) {
        List<RestaurantType> restaurantTypesEntity = searchRestaurant.findAll(page, size);
        /*this.searchRestaurantOutput = new SearchRestaurantTypeOutput(
            restaurantTypesEntity,
            new OutputStatus(200, "Ok", "RestaurantType encontrado")
        );*/
        return restaurantTypesEntity;
    }
}
