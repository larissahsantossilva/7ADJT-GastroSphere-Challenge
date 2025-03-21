package br.com.fiap.gastrosphere.core.domain.usecase.address;

import br.com.fiap.gastrosphere.core.domain.entity.Address;
import br.com.fiap.gastrosphere.core.domain.exception.address.AddressAlreadyExistsException;
import br.com.fiap.gastrosphere.core.domain.gateway.address.AddressInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CreateAddressUseCase {

    private final AddressInterface addressInterface;

    public Address execute(String country, String state, String city, String zipCode, String street, String number, String complement) throws AddressAlreadyExistsException {

        if (addressInterface.existsByZipCode(zipCode)) {
            throw new AddressAlreadyExistsException("Endereço com o mesmo CEP já existe.");
        }

        Address newAddress = new Address(
                country,
                state,
                city,
                zipCode,
                street,
                number,
                complement,
                LocalDate.now(),
                LocalDate.now()
        );

        return addressInterface.save(newAddress);
    }

}
