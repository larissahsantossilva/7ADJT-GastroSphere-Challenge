package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.UserType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import br.com.fiap.gastrosphere.repositories.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserTypeServiceImpl userTypeService;

    private UUID userTypeId;
    private UserType userType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userTypeId = UUID.randomUUID();
        userType = new UserType(userTypeId, "ADMIN", LocalDate.now(), LocalDate.now());
    }

    @Test
    void findAllUserTypes_returnsPagedList() {
        var page = new PageImpl<>(List.of(userType));
        when(userTypeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        var result = userTypeService.findAllUserTypes(0, 10);

        assertThat(result.getContent()).contains(userType);
    }

    @Test
    void findById_whenExists_returnsUserType() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));

        var result = userTypeService.findById(userTypeId);

        assertThat(result).isEqualTo(userType);
    }

    @Test
    void findById_whenNotExists_throwsResourceNotFound() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTypeService.findById(userTypeId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createUserType_successfullySavesUserType() {
        when(userTypeRepository.save(userType)).thenReturn(userType);

        var result = userTypeService.createUserType(userType);

        assertThat(result).isEqualTo(userType);
    }

    @Test
    void createUserType_whenSaveFails_throwsUnprocessableEntity() {
        when(userTypeRepository.save(userType)).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> userTypeService.createUserType(userType))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUserType_successfullyUpdatesUserType() {
        UserType updatedType = new UserType(null, "NEW_NAME", null, null);

        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        var result = userTypeService.updateUserType(userTypeId, updatedType);

        assertThat(result.getName()).isEqualTo("NEW_NAME");
    }

    @Test
    void updateUserType_whenNotFound_throwsResourceNotFound() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTypeService.updateUserType(userTypeId, userType))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateUserType_whenSaveFails_throwsUnprocessableEntity() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> userTypeService.updateUserType(userTypeId, userType))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void deleteUserTypeById_successfullyDeletes() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userRepository.existsByUserType_Id(userTypeId)).thenReturn(false);
        doNothing().when(userTypeRepository).deleteById(userTypeId);

        userTypeService.deleteUserTypeById(userTypeId);

        verify(userTypeRepository).deleteById(userTypeId);
    }

    @Test
    void deleteUserTypeById_whenNotFound_throwsResourceNotFound() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTypeService.deleteUserTypeById(userTypeId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteUserTypeById_whenInUse_throwsUnprocessableEntity() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userRepository.existsByUserType_Id(userTypeId)).thenReturn(true);

        assertThatThrownBy(() -> userTypeService.deleteUserTypeById(userTypeId))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void deleteUserTypeById_whenDeleteFails_throwsUnprocessableEntity() {
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userRepository.existsByUserType_Id(userTypeId)).thenReturn(false);
        doThrow(mock(DataAccessException.class)).when(userTypeRepository).deleteById(userTypeId);

        assertThatThrownBy(() -> userTypeService.deleteUserTypeById(userTypeId))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUserType_shouldNotUpdateName_whenNull() {
        UserType updated = new UserType(null, null, null, null);

        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        var result = userTypeService.updateUserType(userTypeId, updated);

        assertThat(result.getName()).isEqualTo(userType.getName());
    }

}
