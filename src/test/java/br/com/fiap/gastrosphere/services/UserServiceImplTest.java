package br.com.fiap.gastrosphere.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gastrosphere.core.application.service.UserServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;
import br.com.fiap.gastrosphere.core.infra.repository.UserRepository;
import br.com.fiap.gastrosphere.core.infra.repository.UserTypeRepository;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTypeRepository userTypeRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userTypeRepository = mock(UserTypeRepository.class);
        userService = new UserServiceImpl(userRepository, userTypeRepository);
    }

    @Test
    void findAllUsers_shouldReturnPageOfUsers() {
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new UserModel())));

        Page<UserModel> result = userService.findAllUsers(0, 10);

        assertThat(result.getContent()).hasSize(1);
        verify(userRepository).findAll(any(org.springframework.data.domain.Pageable.class));

    }

    @Test
    void findById_shouldReturnUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new UserModel()));

        UserModel result = userService.findById(id);

        assertThat(result).isNotNull();
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createUser_shouldPersistUser() {
    	UserModel user = new UserModel();
        user.setUserType(new UserTypeModel());
        when(userTypeRepository.findByName(any())).thenReturn(new UserTypeModel());
        when(userRepository.save(user)).thenReturn(user);

        UserModel result = userService.createUser(user);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void createUser_shouldThrowIfUserTypeNotFound() {
    	UserModel user = new UserModel();
        UserTypeModel userType = new UserTypeModel();
        userType.setName("ADMIN");
        user.setUserType(userType);

        when(userTypeRepository.findByName("ADMIN")).thenReturn(null);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void createUser_shouldThrowOnDataAccessException() {
    	UserModel user = new UserModel();
        when(userRepository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUser_shouldUpdateFields() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setName("New Name");
        user.setUserType(new UserTypeModel());

        UserModel existing = new UserModel();
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userTypeRepository.findByName(any())).thenReturn(new UserTypeModel());
        when(userRepository.save(any())).thenReturn(existing);

        UserModel result = userService.updateUser(user, id);

        assertThat(result.getName()).isEqualTo("New Name");
    }

    @Test
    void updateUser_shouldThrowIfUserTypeNotFound() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        UserTypeModel userType = new UserTypeModel();
        userType.setName("ADMIN");
        user.setUserType(userType);

        when(userRepository.findById(id)).thenReturn(Optional.of(new UserModel()));
        when(userTypeRepository.findByName("ADMIN")).thenReturn(null);

        assertThatThrownBy(() -> userService.updateUser(user, id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUser_shouldThrowOnDataAccessException() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        when(userRepository.findById(id)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> userService.updateUser(user, id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updatePassword_shouldUpdateSuccessfully() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setPassword("old");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.updatePassword(eq(id), eq("old"), eq("new"), any()))
                .thenReturn(1);

        assertThatCode(() -> userService.updatePassword(id, "old", "new"))
                .doesNotThrowAnyException();
    }

    @Test
    void updatePassword_shouldThrowIfOldPasswordInvalid() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setPassword("correct");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updatePassword(id, "wrong", "new"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updatePassword_shouldThrowIfNewPasswordSame() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setPassword("same");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updatePassword(id, "same", "same"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updatePassword_shouldThrowIfRepositoryReturnsZero() {
        UUID id = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setPassword("old");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.updatePassword(any(), any(), any(), any())).thenReturn(0);

        assertThatThrownBy(() -> userService.updatePassword(id, "old", "new"))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void deleteUserById_shouldExecuteSuccessfully() {
        UUID id = UUID.randomUUID();

        assertThatCode(() -> userService.deleteUserById(id)).doesNotThrowAnyException();
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUserById_shouldThrowOnException() {
        UUID id = UUID.randomUUID();
        doThrow(mock(DataAccessException.class)).when(userRepository).deleteById(id);

        assertThatThrownBy(() -> userService.deleteUserById(id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUser_shouldUpdateOnlyName() {
    	UserModel update = new UserModel();
        update.setName("Novo Nome");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any(UserModel.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getName()).isEqualTo("Novo Nome");
    }

    @Test
    void updateUser_shouldUpdateOnlyEmail() {
    	UserModel update = new UserModel();
        update.setEmail("novo@email.com");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any(UserModel.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getEmail()).isEqualTo("novo@email.com");
    }

    @Test
    void updateUser_shouldUpdateOnlyLogin() {
    	UserModel update = new UserModel();
        update.setLogin("novologin");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any(UserModel.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getLogin()).isEqualTo("novologin");
    }

    @Test
    void updateUser_shouldUpdateOnlyPassword() {
    	UserModel update = new UserModel();
        update.setPassword("novasenha");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any(UserModel.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getPassword()).isEqualTo("novasenha");
    }

    @Test
    void updateUser_shouldUpdateOnlyDocument() {
    	UserModel update = new UserModel();
        update.setDocument("99999999999");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any(UserModel.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getDocument()).isEqualTo("99999999999");
    }

    @Test
    void updateUser_shouldUpdateOnlyAddress() {
    	UserModel update = new UserModel();
        update.setAddress(mock(AddressModel.class));

        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserModel()));
        when(userRepository.save(any(UserModel.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getAddress()).isNotNull();
    }

}
