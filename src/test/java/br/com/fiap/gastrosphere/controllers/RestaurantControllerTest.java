package br.com.fiap.gastrosphere.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import br.com.fiap.gastrosphere.core.application.controller.RestaurantController;
import br.com.fiap.gastrosphere.core.application.dto.request.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.core.application.service.RestaurantServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantModel;

class RestaurantControllerTest {

    @Mock
    private RestaurantServiceImpl service;
    @InjectMocks
    private RestaurantController controller;

    private UUID id;
    private RestaurantModel restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        restaurant = new RestaurantModel();
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
