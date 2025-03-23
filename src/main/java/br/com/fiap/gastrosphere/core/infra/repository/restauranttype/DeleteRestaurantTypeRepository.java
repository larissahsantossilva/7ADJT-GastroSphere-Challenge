package br.com.fiap.gastrosphere.core.infra.repository.restauranttype;

import br.com.fiap.gastrosphere.core.application.service.RestaurantTypeServiceImpl;
import br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils;
import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.DeleteRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.SearchRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantTypeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_DELETAR_TIPO_RESTAURANTE;
import static org.slf4j.LoggerFactory.getLogger;

@RequiredArgsConstructor
public class DeleteRestaurantTypeRepository implements DeleteRestaurantTypeInterface {
    private final RestaurantTypeRepository restaurantTypeRepository;
    @Override
    public void deleteById(UUID id) {
        try {
            restaurantTypeRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_TIPO_RESTAURANTE);
        }
    }
}
