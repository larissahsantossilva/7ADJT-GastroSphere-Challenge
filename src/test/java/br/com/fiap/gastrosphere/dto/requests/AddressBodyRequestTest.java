package br.com.fiap.gastrosphere.dto.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.application.dto.request.AddressBodyRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AddressBodyRequestTest {

    private AddressBodyRequest addressBodyRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        addressBodyRequest = new AddressBodyRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        String country = "Brasil";
        String state = "São Paulo";
        String city = "São Paulo";
        String zipCode = "12345-678";
        String street = "Rua das Flores";
        String number = "100A";
        String complement = "Ap 202";

        addressBodyRequest.setCountry(country);
        addressBodyRequest.setState(state);
        addressBodyRequest.setCity(city);
        addressBodyRequest.setZipCode(zipCode);
        addressBodyRequest.setStreet(street);
        addressBodyRequest.setNumber(number);
        addressBodyRequest.setComplement(complement);

        assertEquals(country, addressBodyRequest.getCountry());
        assertEquals(state, addressBodyRequest.getState());
        assertEquals(city, addressBodyRequest.getCity());
        assertEquals(zipCode, addressBodyRequest.getZipCode());
        assertEquals(street, addressBodyRequest.getStreet());
        assertEquals(number, addressBodyRequest.getNumber());
        assertEquals(complement, addressBodyRequest.getComplement());
    }

    @Test
    void testValidationAnnotations() {
        addressBodyRequest.setCountry("");
        addressBodyRequest.setState("");
        addressBodyRequest.setCity("");
        addressBodyRequest.setZipCode("1234567"); 
        addressBodyRequest.setStreet("");
        addressBodyRequest.setNumber("");
        addressBodyRequest.setComplement("Complemento muito longo".repeat(20)); 
        Set<ConstraintViolation<AddressBodyRequest>> violations = validator.validate(addressBodyRequest);

       
        assertFalse(violations.isEmpty());
        assertEquals(12, violations.size()); 
    }

    @Test
    void testValidObject() {
        addressBodyRequest.setCountry("Brasil");
        addressBodyRequest.setState("São Paulo");
        addressBodyRequest.setCity("São Paulo");
        addressBodyRequest.setZipCode("12345-678");
        addressBodyRequest.setStreet("Rua das Flores");
        addressBodyRequest.setNumber("123");
        addressBodyRequest.setComplement("Ap 202");

        Set<ConstraintViolation<AddressBodyRequest>> violations = validator.validate(addressBodyRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        String country = "Brasil";
        String state = "São Paulo";

        addressBodyRequest.setCountry(country);
        addressBodyRequest.setState(state);

        AddressBodyRequest sameAddress = new AddressBodyRequest();
        sameAddress.setCountry(country);
        sameAddress.setState(state);

        AddressBodyRequest differentAddress = new AddressBodyRequest();
        differentAddress.setCountry("Argentina");
        differentAddress.setState("Buenos Aires");

        assertEquals(addressBodyRequest, sameAddress);
        assertEquals(addressBodyRequest.hashCode(), sameAddress.hashCode());
        assertNotEquals(addressBodyRequest, differentAddress);
    }

    @Test
    void testToString() {
        addressBodyRequest.setCountry("Brasil");
        addressBodyRequest.setState("São Paulo");
        String toStringResult = addressBodyRequest.toString();
        assertTrue(toStringResult.contains("Brasil"));
        assertTrue(toStringResult.contains("São Paulo"));
    }
}
