package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.dtos.responses.RestaurantBodyResponse;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantControllerTest {

    @Mock
    private RestaurantServiceImpl service;
    @InjectMocks
    private RestaurantController controller;

    private UUID id;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName("Rango");
    }

    @Test
    void shouldListRestaurants() {
        when(service.findAllRestaurants(0, 10)).thenReturn(new PageImpl<>(List.of(restaurant)));
        var result = controller.findAllRestaurants(0, 10);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldFindById() {
        when(service.findById(id)).thenReturn(restaurant);
        var result = controller.findRestaurantById(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnNotFoundOnNull() {
        when(service.findById(id)).thenReturn(null);
        var result = controller.findRestaurantById(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateRestaurant() {
        RestaurantBodyRequest dto = new RestaurantBodyRequest();
        dto.setName("Novo Restaurante");
        when(service.createRestaurant(any())).thenReturn(restaurant);
        var result = controller.createRestaurant(dto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldUpdateRestaurant() {
        RestaurantBodyRequest dto = new RestaurantBodyRequest();
        var result = controller.updateRestaurant(id, dto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldDeleteRestaurant() {
        var result = controller.deleteRestaurant(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldReturnNotFoundOnDeleteError() {
        doThrow(new UnprocessableEntityException("")).when(service).deleteRestaurantById(id);
        var result = controller.deleteRestaurant(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
