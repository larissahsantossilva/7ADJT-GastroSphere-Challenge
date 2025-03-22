package br.com.fiap.gastrosphere.core.domain.usecase.address;

import br.com.fiap.gastrosphere.core.domain.entity.Address;
import br.com.fiap.gastrosphere.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.address.AddressInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UpdateAddressUseCase {

    private final AddressInterface addressInterface;

    public Address execute(UUID id, String country, String state, String city, String zipCode, String street, String number, String complement) throws AddressNotFoundException {

        Optional<Address> addressOptional = addressInterface.findById(id);

        if (addressOptional.isEmpty()) {
            throw new AddressNotFoundException("Endereço não encontrado.");
        }

        Address address = addressOptional.get();
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);
        address.setZipCode(zipCode);
        address.setStreet(street);
        address.setNumber(number);
        address.setComplement(complement);
        address.setLastModifiedAt(LocalDate.now());

        return addressInterface.save(address);
    }
}
