package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.RestaurantType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.RestaurantTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantTypeServiceImplTest {

    @Mock
    private RestaurantTypeRepository repository;

    @InjectMocks
    private RestaurantTypeServiceImpl service;

    private UUID id;
    private RestaurantType type;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        type = new RestaurantType();
        type.setId(id);
        type.setName("Fast Food");
    }

    @Test
    void shouldFindAllTypes() {
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(type)));
        Page<RestaurantType> result = service.findAllRestaurantTypes(0, 10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindTypeById() {
        when(repository.findById(id)).thenReturn(Optional.of(type));
        RestaurantType result = service.findById(id);
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
        RestaurantType result = service.createRestaurantType(type);
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
        RestaurantType update = new RestaurantType();
        update.setName("Novo Nome");

        when(repository.findById(id)).thenReturn(Optional.of(type));
        when(repository.save(any())).thenReturn(type);

        RestaurantType result = service.updateRestaurantType(update, id);
        assertThat(result).isNotNull();
        verify(repository).save(type);
    }

    @Test
    void shouldUpdateTypeWhenNameIsNull() {
        RestaurantType update = new RestaurantType(); // name == null

        when(repository.findById(id)).thenReturn(Optional.of(type));
        when(repository.save(any())).thenReturn(type);

        RestaurantType result = service.updateRestaurantType(update, id);
        assertThat(result).isNotNull();
        verify(repository).save(type);
    }

    @Test
    void shouldThrowWhenUpdateFails() {
        RestaurantType update = new RestaurantType();
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
