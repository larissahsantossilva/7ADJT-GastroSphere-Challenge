package br.com.fiap.gastrosphere.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.fiap.gastrosphere.core.application.service.RestaurantServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantRepository;

class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private UUID id;
    private RestaurantModel restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        restaurant = new RestaurantModel();
        restaurant.setId(id);
        restaurant.setName("Restaurante Teste");
        restaurant.setStartedAt(LocalTime.of(8, 0));
        restaurant.setFinishedAt(LocalTime.of(20, 0));
    }

    @Test
    void shouldFindAllRestaurants() {
        when(restaurantRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(restaurant)));

        Page<RestaurantModel> result = restaurantService.findAllRestaurants(0, 10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindRestaurantById() {
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        RestaurantModel found = restaurantService.findById(id);
        assertThat(found).isEqualTo(restaurant);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> restaurantService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldCreateRestaurant() {
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        RestaurantModel result = restaurantService.createRestaurant(restaurant);
        assertThat(result).isEqualTo(restaurant);
    }

    @Test
    void shouldThrowWhenCreateFails() {
        when(restaurantRepository.save(restaurant)).thenThrow(mock(DataAccessException.class));
        assertThatThrownBy(() -> restaurantService.createRestaurant(restaurant))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldUpdateRestaurant() {
    	RestaurantModel update = new RestaurantModel();
        update.setName("Novo Nome");

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowWhenUpdateFails() {
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenThrow(mock(DataAccessException.class));
        assertThatThrownBy(() -> restaurantService.updateRestaurant(restaurant, id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldDeleteRestaurant() {
        doNothing().when(restaurantRepository).deleteById(id);
        restaurantService.deleteRestaurantById(id);
        verify(restaurantRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeleteFails() {
        doThrow(mock(DataAccessException.class)).when(restaurantRepository).deleteById(id);
        assertThatThrownBy(() -> restaurantService.deleteRestaurantById(id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldUpdateUserWhenPresent() {
    	RestaurantModel update = new RestaurantModel();
        update.setUser(mock(UserModel.class));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateNameWhenPresent() {
    	RestaurantModel update = new RestaurantModel();
        update.setName("Atualizado");

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateAddressWhenPresent() {
    	RestaurantModel update = new RestaurantModel();
        update.setAddress(mock(AddressModel.class));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateRestaurantTypeWhenPresent() {
    	RestaurantModel update = new RestaurantModel();
        update.setRestaurantType(mock(RestaurantTypeModel.class));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateStartedAtWhenPresent() {
    	RestaurantModel update = new RestaurantModel();
        update.setStartedAt(LocalTime.of(10, 0));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateFinishedAtWhenPresent() {
    	RestaurantModel update = new RestaurantModel();
        update.setFinishedAt(LocalTime.of(22, 0));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantModel result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

}
