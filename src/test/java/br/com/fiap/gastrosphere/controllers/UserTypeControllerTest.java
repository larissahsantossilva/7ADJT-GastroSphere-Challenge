package br.com.fiap.gastrosphere.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;

import br.com.fiap.gastrosphere.core.application.controller.UserTypeController;
import br.com.fiap.gastrosphere.core.application.dto.request.UserTypeBodyRequest;
import br.com.fiap.gastrosphere.core.application.service.UserTypeServiceImpl;
import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;

public class UserTypeControllerTest {

    @Mock private UserTypeServiceImpl userTypeService;

    @InjectMocks private UserTypeController controller;

    private UUID userTypeId;
    private UserTypeModel userType;
    private UserTypeBodyRequest dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userTypeId = UUID.randomUUID();
        userType = new UserTypeModel(userTypeId, "ADMIN", LocalDate.now(), LocalDate.now());
        dto = new UserTypeBodyRequest("ADMIN");
    }

    @Test
    void findAllUserTypes_shouldReturnPagedList() {
        var page = new PageImpl<>(List.of(userType));
        when(userTypeService.findAllUserTypes(0, 10)).thenReturn(page);

        var response = controller.findAllUserTypes(0, 10);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains(userType);
    }

    @Test
    void findUserTypeById_shouldReturnUserType_whenExists() {
        when(userTypeService.findById(userTypeId)).thenReturn(userType);

        var response = controller.findUserTypeById(userTypeId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(userType);
    }

    @Test
    void findUserTypeById_shouldReturnNotFound_whenNotExists() {
        when(userTypeService.findById(userTypeId)).thenReturn(null);

        var response = controller.findUserTypeById(userTypeId);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void createUserType_shouldReturnCreatedStatus() {
        when(userTypeService.createUserType(any())).thenReturn(userType);

        var response = controller.createUserType(dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(userTypeId);
    }

    @Test
    void updateUserType_shouldReturnSuccess() {
        when(userTypeService.updateUserType(eq(userTypeId), any())).thenReturn(userType);

        var response = controller.updateUserType(userTypeId, dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Tipo de usuário atualizado com sucesso");
    }

    @Test
    void deleteUserType_shouldReturnNoContent() {
        doNothing().when(userTypeService).deleteUserTypeById(userTypeId);

        var response = controller.deleteUserType(userTypeId);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void deleteUserType_shouldReturnUnprocessableEntity_whenExceptionThrown() {
        doThrow(new UnprocessableEntityException("Tipo associado")).when(userTypeService).deleteUserTypeById(userTypeId);

        var response = controller.deleteUserType(userTypeId);

        assertThat(response.getStatusCodeValue()).isEqualTo(422);
        assertThat(response.getBody()).isEqualTo("Tipo associado");
    }

    @Test
    void deleteUserType_shouldReturnNotFound_whenResourceNotFound() {
        doThrow(new ResourceNotFoundException("Não encontrado")).when(userTypeService).deleteUserTypeById(userTypeId);

        var response = controller.deleteUserType(userTypeId);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Não encontrado");
    }
}
