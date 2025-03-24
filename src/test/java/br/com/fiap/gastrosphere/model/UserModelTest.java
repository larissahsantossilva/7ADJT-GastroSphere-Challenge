package br.com.fiap.gastrosphere.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;

class UserModelTest {

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String name = "João da Silva";
        String email = "joao.silva@example.com";
        String login = "joaosilva";
        String password = "senha123";
        String document = "12345678900";
        UserTypeModel userType = new UserTypeModel();
        AddressModel address = new AddressModel();
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setLogin(login);
        user.setPassword(password);
        user.setDocument(document);
        user.setUserType(userType);
        user.setAddress(address);
        user.setCreatedAt(createdAt);
        user.setLastModifiedAt(lastModifiedAt);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(login, user.getLogin());
        assertEquals(password, user.getPassword());
        assertEquals(document, user.getDocument());
        assertEquals(userType, user.getUserType());
        assertEquals(address, user.getAddress());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(lastModifiedAt, user.getLastModifiedAt());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getLastModifiedAt());
        assertEquals(LocalDate.now(), user.getCreatedAt());
        assertEquals(LocalDate.now(), user.getLastModifiedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String document = "12345678900";

        user.setId(id);
        user.setDocument(document);

        UserModel sameUser = new UserModel();
        sameUser.setId(id);
        sameUser.setDocument(document);

        UserModel differentUser = new UserModel();
        differentUser.setId(UUID.randomUUID());
        differentUser.setDocument("98765432100");

        assertEquals(user, sameUser);
        assertEquals(user.hashCode(), sameUser.hashCode());
        assertNotEquals(user, differentUser);
    }

    @Test
    void testNoArgsConstructor() {
        UserModel newUser = new UserModel();
        assertNotNull(newUser.getCreatedAt());
        assertNotNull(newUser.getLastModifiedAt());
        assertNull(newUser.getId());
        assertNull(newUser.getName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getLogin());
        assertNull(newUser.getPassword());
        assertNull(newUser.getDocument());
        assertNull(newUser.getUserType());
        assertNull(newUser.getAddress());
    }

    @Test
    void testToString() {
        user.setId(UUID.randomUUID());
        user.setName("Maria dos Santos");
        String toStringResult = user.toString();
        assertTrue(toStringResult.contains("Maria dos Santos"));
        assertTrue(toStringResult.contains("id="));
    }
}
