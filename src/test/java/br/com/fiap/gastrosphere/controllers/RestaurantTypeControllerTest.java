package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.RestaurantTypeBodyRequest;
import br.com.fiap.gastrosphere.entities.RestaurantType;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.RestaurantTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantTypeControllerTest {

    @Mock
    private RestaurantTypeServiceImpl service;
    @InjectMocks
    private RestaurantTypeController controller;

    private UUID id;
    private RestaurantType type;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        type = new RestaurantType();
        type.setId(id);
        type.setName("Brasileira");
    }

    @Test
    void shouldListAllTypes() {
        when(service.findAllRestaurantTypes(0, 10)).thenReturn(new PageImpl<>(List.of(type)));
        var result = controller.findAllRestaurantTypes(0, 10);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldFindById() {
        when(service.findById(id)).thenReturn(type);
        var result = controller.findRestaurantTypeById(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnNotFound() {
        when(service.findById(id)).thenReturn(null);
        var result = controller.findRestaurantTypeById(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateType() {
        RestaurantTypeBodyRequest dto = new RestaurantTypeBodyRequest("Churrascaria");
        when(service.createRestaurantType(any())).thenReturn(type);
        var result = controller.createRestaurantType(dto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldUpdateType() {
        RestaurantTypeBodyRequest dto = new RestaurantTypeBodyRequest("Italiana");
        var result = controller.updateRestaurantType(id, dto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldDeleteType() {
        var result = controller.deleteRestaurantType(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldReturnNotFoundOnDeleteFail() {
        doThrow(new UnprocessableEntityException("")).when(service).deleteRestaurantTypeById(id);
        var result = controller.deleteRestaurantType(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
