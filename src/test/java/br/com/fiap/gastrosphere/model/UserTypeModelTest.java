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

import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;

class UserTypeModelTest {

    private UserTypeModel userType;

    @BeforeEach
    void setUp() {
        userType = new UserTypeModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String name = "Administrador";
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);

        userType.setId(id);
        userType.setName(name);
        userType.setCreatedAt(createdAt);
        userType.setLastModifiedAt(lastModifiedAt);

        assertEquals(id, userType.getId());
        assertEquals(name, userType.getName());
        assertEquals(createdAt, userType.getCreatedAt());
        assertEquals(lastModifiedAt, userType.getLastModifiedAt());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(userType.getCreatedAt());
        assertNotNull(userType.getLastModifiedAt());
        assertEquals(LocalDate.now(), userType.getCreatedAt());
        assertEquals(LocalDate.now(), userType.getLastModifiedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String name = "Administrador";

        userType.setId(id);
        userType.setName(name);

        UserTypeModel sameUserType = new UserTypeModel();
        sameUserType.setId(id);
        sameUserType.setName(name);

        UserTypeModel differentUserType = new UserTypeModel();
        differentUserType.setId(UUID.randomUUID());
        differentUserType.setName("Usuário");

        assertEquals(userType, sameUserType);
        assertEquals(userType.hashCode(), sameUserType.hashCode());
        assertNotEquals(userType, differentUserType);
    }

    @Test
    void testNoArgsConstructor() {
        UserTypeModel newUserType = new UserTypeModel();
        assertNotNull(newUserType.getCreatedAt());
        assertNotNull(newUserType.getLastModifiedAt());
        assertNull(newUserType.getId());
        assertNull(newUserType.getName());
    }

    @Test
    void testToString() {
        userType.setId(UUID.randomUUID());
        userType.setName("Administrador");
        String toStringResult = userType.toString();
        assertTrue(toStringResult.contains("Administrador"));
        assertTrue(toStringResult.contains("id="));
    }
}
