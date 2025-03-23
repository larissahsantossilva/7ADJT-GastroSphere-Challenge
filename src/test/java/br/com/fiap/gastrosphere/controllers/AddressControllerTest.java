package br.com.fiap.gastrosphere.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.gastrosphere.core.application.controller.AddressController;
import br.com.fiap.gastrosphere.core.application.dto.request.AddressBodyRequest;
import br.com.fiap.gastrosphere.core.application.service.AddressServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.AddressModel;

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
        Page<AddressModel> page = new PageImpl<>(Collections.singletonList(new AddressModel()));
        when(addressService.findAllAddresses(0, 10)).thenReturn(page);

        ResponseEntity<Page<AddressModel>> response = controller.findAllAddresses(0, 10, null);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAllAddresses(0, 10);
    }

    @Test
    void deveListarEnderecosComZipCode() {
        String zip = "12345678";
        Page<AddressModel> page = new PageImpl<>(Collections.singletonList(new AddressModel()));
        when(addressService.findAddressByZipCode(zip, 0, 10)).thenReturn(page);

        ResponseEntity<Page<AddressModel>> response = controller.findAllAddresses(0, 10, zip);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAddressByZipCode(zip, 0, 10);
    }

    @Test
    void deveBuscarEnderecoPorId_Encontrado() {
        UUID id = UUID.randomUUID();
        AddressModel address = new AddressModel();
        when(addressService.findById(id)).thenReturn(address);

        ResponseEntity<AddressModel> response = controller.findAddressById(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(address);
    }

    @Test
    void deveRetornar404_QuandoEnderecoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(addressService.findById(id)).thenReturn(null);

        ResponseEntity<AddressModel> response = controller.findAddressById(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deveCriarEndereco_ComSucesso() {
        AddressBodyRequest request = new AddressBodyRequest();
        UUID id = UUID.randomUUID();
        AddressModel address = new AddressModel();
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

        AddressModel mockAddress = new AddressModel();
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
    	AddressModel address = new AddressModel();
        Page<AddressModel> page = new PageImpl<>(List.of(address));
        when(addressService.findAllAddresses(0, 10)).thenReturn(page);

        ResponseEntity<?> response = controller.findAllAddresses(0, 10, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAllAddresses(0, 10);
    }

    @Test
    void shouldFindAllAddressesWhenZipCodeIsEmpty() {
    	AddressModel address = new AddressModel();
        Page<AddressModel> page = new PageImpl<>(List.of(address));
        when(addressService.findAllAddresses(0, 10)).thenReturn(page);

        ResponseEntity<?> response = controller.findAllAddresses(0, 10, "");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(addressService).findAllAddresses(0, 10);
    }

}
