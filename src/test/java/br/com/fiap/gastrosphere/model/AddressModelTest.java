package br.com.fiap.gastrosphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.infra.model.AddressModel;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddressModelTest {

    private AddressModel address;

    @BeforeEach
    void setUp() {
        address = new AddressModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String country = "Brasil";
        String state = "São Paulo";
        String city = "São Paulo";
        String zipCode = "12345-678";
        String street = "Rua Teste";
        String number = "100";
        String complement = "Ap 202";
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);

        address.setId(id);
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);
        address.setZipCode(zipCode);
        address.setStreet(street);
        address.setNumber(number);
        address.setComplement(complement);
        address.setCreatedAt(createdAt);
        address.setLastModifiedAt(lastModifiedAt);

        assertEquals(id, address.getId());
        assertEquals(country, address.getCountry());
        assertEquals(state, address.getState());
        assertEquals(city, address.getCity());
        assertEquals(zipCode, address.getZipCode());
        assertEquals(street, address.getStreet());
        assertEquals(number, address.getNumber());
        assertEquals(complement, address.getComplement());
        assertEquals(createdAt, address.getCreatedAt());
        assertEquals(lastModifiedAt, address.getLastModifiedAt());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(address.getCreatedAt());
        assertNotNull(address.getLastModifiedAt());
        assertEquals(LocalDate.now(), address.getCreatedAt());
        assertEquals(LocalDate.now(), address.getLastModifiedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        address.setId(id);
        address.setCountry("Brasil");
        address.setState("São Paulo");
        address.setCity("São Paulo");
        address.setZipCode("12345-678");
        address.setStreet("Rua Teste");
        address.setNumber("100");
        address.setComplement("Ap 202");

        AddressModel sameAddress = new AddressModel();
        sameAddress.setId(id);
        sameAddress.setCountry("Brasil");
        sameAddress.setState("São Paulo");
        sameAddress.setCity("São Paulo");
        sameAddress.setZipCode("12345-678");
        sameAddress.setStreet("Rua Teste");
        sameAddress.setNumber("100");
        sameAddress.setComplement("Ap 202");

        AddressModel differentAddress = new AddressModel();
        differentAddress.setId(UUID.randomUUID());
        differentAddress.setCountry("Argentina");

        assertEquals(address, sameAddress);
        assertEquals(address.hashCode(), sameAddress.hashCode());
        assertNotEquals(address, differentAddress);
    }

    @Test
    void testNoArgsConstructor() {
        AddressModel newAddress = new AddressModel();
        assertNotNull(newAddress.getCreatedAt());
        assertNotNull(newAddress.getLastModifiedAt());
    }
}
