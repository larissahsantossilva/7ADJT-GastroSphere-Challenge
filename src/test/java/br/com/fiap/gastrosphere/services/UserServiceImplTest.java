package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.entities.UserType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import br.com.fiap.gastrosphere.repositories.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
                .thenReturn(new PageImpl<>(List.of(new User())));

        Page<User> result = userService.findAllUsers(0, 10);

        assertThat(result.getContent()).hasSize(1);
        verify(userRepository).findAll(any(org.springframework.data.domain.Pageable.class));

    }

    @Test
    void findById_shouldReturnUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        User result = userService.findById(id);

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
        User user = new User();
        user.setUserType(new UserType());
        when(userTypeRepository.findByName(any())).thenReturn(new UserType());
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void createUser_shouldThrowIfUserTypeNotFound() {
        User user = new User();
        UserType userType = new UserType();
        userType.setName("ADMIN");
        user.setUserType(userType);

        when(userTypeRepository.findByName("ADMIN")).thenReturn(null);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void createUser_shouldThrowOnDataAccessException() {
        User user = new User();
        when(userRepository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUser_shouldUpdateFields() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setName("New Name");
        user.setUserType(new UserType());

        User existing = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userTypeRepository.findByName(any())).thenReturn(new UserType());
        when(userRepository.save(any())).thenReturn(existing);

        User result = userService.updateUser(user, id);

        assertThat(result.getName()).isEqualTo("New Name");
    }

    @Test
    void updateUser_shouldThrowIfUserTypeNotFound() {
        UUID id = UUID.randomUUID();
        User user = new User();
        UserType userType = new UserType();
        userType.setName("ADMIN");
        user.setUserType(userType);

        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));
        when(userTypeRepository.findByName("ADMIN")).thenReturn(null);

        assertThatThrownBy(() -> userService.updateUser(user, id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updateUser_shouldThrowOnDataAccessException() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenThrow(mock(DataAccessException.class));

        assertThatThrownBy(() -> userService.updateUser(user, id))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void updatePassword_shouldUpdateSuccessfully() {
        UUID id = UUID.randomUUID();
        User user = new User();
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
        User user = new User();
        user.setPassword("correct");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updatePassword(id, "wrong", "new"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updatePassword_shouldThrowIfNewPasswordSame() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setPassword("same");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updatePassword(id, "same", "same"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updatePassword_shouldThrowIfRepositoryReturnsZero() {
        UUID id = UUID.randomUUID();
        User user = new User();
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
        User update = new User();
        update.setName("Novo Nome");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getName()).isEqualTo("Novo Nome");
    }

    @Test
    void updateUser_shouldUpdateOnlyEmail() {
        User update = new User();
        update.setEmail("novo@email.com");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getEmail()).isEqualTo("novo@email.com");
    }

    @Test
    void updateUser_shouldUpdateOnlyLogin() {
        User update = new User();
        update.setLogin("novologin");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getLogin()).isEqualTo("novologin");
    }

    @Test
    void updateUser_shouldUpdateOnlyPassword() {
        User update = new User();
        update.setPassword("novasenha");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getPassword()).isEqualTo("novasenha");
    }

    @Test
    void updateUser_shouldUpdateOnlyDocument() {
        User update = new User();
        update.setDocument("99999999999");
        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getDocument()).isEqualTo("99999999999");
    }

    @Test
    void updateUser_shouldUpdateOnlyAddress() {
        User update = new User();
        update.setAddress(mock(Address.class));

        UUID userId = new UUID(0, 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(update);

        var result = userService.updateUser(update, userId);

        assertThat(result.getAddress()).isNotNull();
    }

}
