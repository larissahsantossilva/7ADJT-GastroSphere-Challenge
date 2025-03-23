package br.com.fiap.gastrosphere.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import br.com.fiap.gastrosphere.core.application.service.RestaurantTypeServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantTypeRepository;

class RestaurantTypeServiceImplTest {

    @Mock
    private RestaurantTypeRepository repository;

    @InjectMocks
    private RestaurantTypeServiceImpl service;

    private UUID id;
    private RestaurantTypeModel type;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        type = new RestaurantTypeModel();
        type.setId(id);
        type.setName("Fast Food");
    }

    @Test
    void shouldFindAllTypes() {
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(type)));
        Page<RestaurantTypeModel> result = service.findAllRestaurantTypes(0, 10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindTypeById() {
        when(repository.findById(id)).thenReturn(Optional.of(type));
        RestaurantTypeModel result = service.findById(id);
        assertThat(result).isEqualTo(type);
    }

    @Test
    void shouldThrowWhenTypeNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldCreateType() {
        when(repository.save(type)).thenReturn(type);
        RestaurantTypeModel result = service.createRestaurantType(type);
        assertThat(result).isEqualTo(type);
    }

    @Test
    void shouldThrowWhenCreateFails() {
        when(repository.save(type)).thenThrow(mock(DataAccessException.class));
        assertThatThrownBy(() -> service.createRestaurantType(type))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldUpdateTypeWhenNamePresent() {
    	RestaurantTypeModel update = new RestaurantTypeModel();
        update.setName("Novo Nome");

        when(repository.findById(id)).thenReturn(Optional.of(type));
        when(repository.save(any())).thenReturn(type);

        RestaurantTypeModel result = service.updateRestaurantType(update, id);
        assertThat(result).isNotNull();
        verify(repository).save(type);
    }

    @Test
    void shouldUpdateTypeWhenNameIsNull() {
    	RestaurantTypeModel update = new RestaurantTypeModel(); // name == null

        when(repository.findById(id)).thenReturn(Optional.of(type));
        when(repository.save(any())).thenReturn(type);

        RestaurantTypeModel result = service.updateRestaurantType(update, id);
        assertThat(result).isNotNull();
        verify(repository).save(type);
    }

    @Test
    void shouldThrowWhenUpdateFails() {
    	RestaurantTypeModel update = new RestaurantTypeModel();
        update.setName("Novo Nome");

        when(repository.findById(id)).thenReturn(Optional.of(type));
        when(repository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> service.updateRestaurantType(update, id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldDeleteType() {
        doNothing().when(repository).deleteById(id);
        service.deleteRestaurantTypeById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeleteFails() {
        doThrow(mock(DataAccessException.class)).when(repository).deleteById(id);
        assertThatThrownBy(() -> service.deleteRestaurantTypeById(id))
                .isInstanceOf(UnprocessableEntityException.class);
    }
}
