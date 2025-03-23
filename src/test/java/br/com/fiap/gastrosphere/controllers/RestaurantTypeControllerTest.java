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

import br.com.fiap.gastrosphere.core.application.controller.RestaurantTypeController;
import br.com.fiap.gastrosphere.core.application.dto.request.RestaurantTypeBodyRequest;
import br.com.fiap.gastrosphere.core.application.service.RestaurantTypeServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;

class RestaurantTypeControllerTest {

    @Mock
    private RestaurantTypeServiceImpl service;
    @InjectMocks
    private RestaurantTypeController controller;

    private UUID id;
    private RestaurantTypeModel type;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        type = new RestaurantTypeModel();
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
