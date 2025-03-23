package br.com.fiap.gastrosphere.core.domain.usecase.restauranttype;

import br.com.fiap.gastrosphere.core.application.service.RestaurantTypeServiceImpl;
import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.domain.gateway.restauranttype.DeleteRestaurantTypeInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;

import java.util.UUID;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_DELETAR_TIPO_RESTAURANTE;
import static org.slf4j.LoggerFactory.getLogger;

@Getter
@RequiredArgsConstructor
public class DeleteByIdRestaurantTypeUseCase {
    private final DeleteRestaurantTypeInterface deleteRestaurant;
    private OutputInterface searchRestaurantOutput;
    private static final Logger logger = getLogger(DeleteByIdRestaurantTypeUseCase.class);

    public void execute(UUID id) {
        try {
            deleteRestaurant.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_TIPO_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_TIPO_RESTAURANTE);
        }
    }
}
