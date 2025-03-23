package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.AddressBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    private AddressServiceImpl addressService;
    private AddressController controller;

    @BeforeEach
    void setUp() {
        addressService = mock(AddressServiceImpl.class);
        controller = new AddressController(addressService);
    }

    @Test
    void deveListarTodosEnderecosSemZipCode() {
        Page<Address> page = new PageImpl<>(Collections.singletonList(new Address()));
        when(addressService.findAllAddresses(0, 10)).thenReturn(page);

        ResponseEntity<Page<Address>> response = controller.findAllAddresses(0, 10, null);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAllAddresses(0, 10);
    }

    @Test
    void deveListarEnderecosComZipCode() {
        String zip = "12345678";
        Page<Address> page = new PageImpl<>(Collections.singletonList(new Address()));
        when(addressService.findAddressByZipCode(zip, 0, 10)).thenReturn(page);

        ResponseEntity<Page<Address>> response = controller.findAllAddresses(0, 10, zip);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAddressByZipCode(zip, 0, 10);
    }

    @Test
    void deveBuscarEnderecoPorId_Encontrado() {
        UUID id = UUID.randomUUID();
        Address address = new Address();
        when(addressService.findById(id)).thenReturn(address);

        ResponseEntity<Address> response = controller.findAddressById(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(address);
    }

    @Test
    void deveRetornar404_QuandoEnderecoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(addressService.findById(id)).thenReturn(null);

        ResponseEntity<Address> response = controller.findAddressById(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deveCriarEndereco_ComSucesso() {
        AddressBodyRequest request = new AddressBodyRequest();
        UUID id = UUID.randomUUID();
        Address address = new Address();
        address.setId(id);

        when(addressService.createAddress(any())).thenReturn(address);

        ResponseEntity<UUID> response = controller.createAddress(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(id);
    }

    @Test
    void deveAtualizarEndereco_ComSucesso() {
        UUID id = UUID.randomUUID();
        AddressBodyRequest request = new AddressBodyRequest();

        Address mockAddress = new Address();
        when(addressService.updateAddress(eq(id), any())).thenReturn(mockAddress);

        ResponseEntity<String> response = controller.updateAddress(id, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Endereço atualizado com sucesso");
    }

    @Test
    void deveExcluirEndereco_ComSucesso() {
        UUID id = UUID.randomUUID();
        doNothing().when(addressService).deleteAddressById(id);

        ResponseEntity<String> response = controller.deleteAddress(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deveRetornarErro_ExcluirEnderecoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        doThrow(new UnprocessableEntityException("Endereço não encontrado"))
                .when(addressService).deleteAddressById(id);

        ResponseEntity<String> response = controller.deleteAddress(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Endereço não encontrado");
    }

    @Test
    void shouldFindAllAddressesWhenZipCodeIsNull() {
        Address address = new Address();
        Page<Address> page = new PageImpl<>(List.of(address));
        when(addressService.findAllAddresses(0, 10)).thenReturn(page);

        ResponseEntity<?> response = controller.findAllAddresses(0, 10, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAllAddresses(0, 10);
    }

    @Test
    void shouldFindAllAddressesWhenZipCodeIsEmpty() {
        Address address = new Address();
        Page<Address> page = new PageImpl<>(List.of(address));
        when(addressService.findAllAddresses(0, 10)).thenReturn(page);

        ResponseEntity<?> response = controller.findAllAddresses(0, 10, "");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAllAddresses(0, 10);
    }

}
