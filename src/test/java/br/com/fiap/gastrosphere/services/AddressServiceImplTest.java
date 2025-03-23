package br.com.fiap.gastrosphere.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.fiap.gastrosphere.core.application.service.AddressServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantModel;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import br.com.fiap.gastrosphere.core.infra.repository.AddressRepository;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantRepository;
import br.com.fiap.gastrosphere.core.infra.repository.UserRepository;

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private AddressServiceImpl service;

    private UUID id;
    private AddressModel address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        address = new AddressModel();
        address.setId(id);
        address.setCountry("Brasil");
        address.setState("SP");
        address.setCity("São Paulo");
        address.setZipCode("12345-678");
        address.setStreet("Rua X");
        address.setNumber("100");
        address.setComplement("Apto 2");
    }

    @Test
    void shouldFindAllAddresses() {
        when(addressRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(address)));

        Page<AddressModel> result = service.findAllAddresses(0, 10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindAddressById() {
        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        AddressModel result = service.findById(id);
        assertThat(result).isEqualTo(address);
    }

    @Test
    void shouldThrowWhenIdNotFound() {
        when(addressRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldFindByZipCode() {
        when(addressRepository.findByZipCode("12345-678", PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(address)));

        Page<AddressModel> result = service.findAddressByZipCode("12345-678", 0, 10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldCreateAddress() {
        when(addressRepository.save(address)).thenReturn(address);
        AddressModel result = service.createAddress(address);
        assertThat(result).isEqualTo(address);
    }

    @Test
    void shouldThrowWhenCreateFails() {
        when(addressRepository.save(address)).thenThrow(mock(DataAccessException.class));
        assertThatThrownBy(() -> service.createAddress(address))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldUpdateAddressWithAllFields() {
        when(addressRepository.findById(id)).thenReturn(Optional.of(new AddressModel()));
        when(addressRepository.save(any())).thenReturn(address);

        AddressModel result = service.updateAddress(id, address);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowWhenUpdateFails() {
        when(addressRepository.findById(id)).thenReturn(Optional.of(new AddressModel()));
        when(addressRepository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> service.updateAddress(id, address))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldThrowWhenAddressToUpdateNotFound() {
        when(addressRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateAddress(id, address))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldDeleteAddressSuccessfully() {
        when(userRepository.findByAddressId(id)).thenReturn(Optional.empty());
        when(restaurantRepository.findByAddressId(id)).thenReturn(Optional.empty());

        service.deleteAddressById(id);

        verify(addressRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenAddressAssociatedToUser() {
        when(userRepository.findByAddressId(id)).thenReturn(Optional.of(mock(UserModel.class)));

        assertThatThrownBy(() -> service.deleteAddressById(id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldLogWhenAddressAssociatedToRestaurant() {
        when(userRepository.findByAddressId(id)).thenReturn(Optional.empty());
        when(restaurantRepository.findByAddressId(id)).thenReturn(Optional.of(mock(RestaurantModel.class)));

        service.deleteAddressById(id);

        verify(addressRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeleteFails() {
        when(userRepository.findByAddressId(id)).thenReturn(Optional.empty());
        when(restaurantRepository.findByAddressId(id)).thenReturn(Optional.empty());
        doThrow(mock(DataAccessException.class)).when(addressRepository).deleteById(id);

        assertThatThrownBy(() -> service.deleteAddressById(id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void shouldUpdateAddressWithNullFields() {
    	AddressModel incoming = new AddressModel(); // all fields null
        when(addressRepository.findById(id)).thenReturn(Optional.of(new AddressModel()));
        when(addressRepository.save(any())).thenReturn(address);

        AddressModel result = service.updateAddress(id, incoming);
        assertThat(result).isNotNull();
    }
}
