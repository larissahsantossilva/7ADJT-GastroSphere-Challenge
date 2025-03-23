package br.com.fiap.gastrosphere.core.domain.usecase.restauranttype;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.SearchRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputStatus;
import br.com.fiap.gastrosphere.core.domain.output.restauranttype.SearchRestaurantTypeOutput;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.UUID;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_CRIAR_TIPO_RESTAURANTE;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ID_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Getter
@RequiredArgsConstructor
public class SearchByIdRestaurantTypeUseCase {
    private final SearchRestaurantTypeInterface searchRestaurant;
    private OutputInterface searchRestaurantOutput;

    public RestaurantType execute(UUID id) {
        uuidValidator(id);
        RestaurantType restaurantTypeEntity = searchRestaurant.findById(id);
        if(restaurantTypeEntity == null) {
            throw new ResourceNotFoundException(ID_NAO_ENCONTRADO);
        }
        return restaurantTypeEntity;
    }
}
