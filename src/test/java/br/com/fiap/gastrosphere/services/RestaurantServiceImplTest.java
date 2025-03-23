package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.entities.RestaurantType;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private UUID id;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName("Restaurante Teste");
        restaurant.setStartedAt(LocalTime.of(8, 0));
        restaurant.setFinishedAt(LocalTime.of(20, 0));
    }

    @Test
    void shouldFindAllRestaurants() {
        when(restaurantRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(restaurant)));

        Page<Restaurant> result = restaurantService.findAllRestaurants(0, 10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindRestaurantById() {
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        Restaurant found = restaurantService.findById(id);
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
        Restaurant result = restaurantService.createRestaurant(restaurant);
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
        Restaurant update = new Restaurant();
        update.setName("Novo Nome");

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
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
        Restaurant update = new Restaurant();
        update.setUser(mock(User.class));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateNameWhenPresent() {
        Restaurant update = new Restaurant();
        update.setName("Atualizado");

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateAddressWhenPresent() {
        Restaurant update = new Restaurant();
        update.setAddress(mock(Address.class));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateRestaurantTypeWhenPresent() {
        Restaurant update = new Restaurant();
        update.setRestaurantType(mock(RestaurantType.class));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateStartedAtWhenPresent() {
        Restaurant update = new Restaurant();
        update.setStartedAt(LocalTime.of(10, 0));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateFinishedAtWhenPresent() {
        Restaurant update = new Restaurant();
        update.setFinishedAt(LocalTime.of(22, 0));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant result = restaurantService.updateRestaurant(update, id);
        assertThat(result).isNotNull();
    }

}
