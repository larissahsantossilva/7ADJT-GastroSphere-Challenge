package br.com.fiap.gastrosphere.core.domain.usecase.restauranttype;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.SearchRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputStatus;
import br.com.fiap.gastrosphere.core.domain.output.restauranttype.SearchRestaurantTypeOutput;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class SearchByIdRestaurantTypeUseCase {
    private final SearchRestaurantTypeInterface searchRestaurant;
    private OutputInterface searchRestaurantOutput;

    public RestaurantType execute(UUID id) {
        return searchRestaurant.findById(id);
    }
}
