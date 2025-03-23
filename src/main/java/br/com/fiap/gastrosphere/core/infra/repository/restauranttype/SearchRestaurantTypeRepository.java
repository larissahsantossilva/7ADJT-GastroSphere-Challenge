package br.com.fiap.gastrosphere.core.infra.repository.restauranttype;

import br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils;
import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.SearchRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SearchRestaurantTypeRepository implements SearchRestaurantTypeInterface {

    private final RestaurantTypeRepository restaurantTypeRepository;

    @Override
    public List<RestaurantType> findAll(int page, int size) {
        Page<RestaurantTypeModel> restaurantTypeModels = restaurantTypeRepository.findAll(PageRequest.of(page, size));
        return restaurantTypeModels.stream()
                .map(GastroSphereUtils::convertToRestaurantType)
                .toList();
    }

    @Override
    public RestaurantType findById(UUID id) {
        return restaurantTypeRepository.findById(id)
                .map(GastroSphereUtils::convertToRestaurantType)
                .orElse(null);
    }
}
