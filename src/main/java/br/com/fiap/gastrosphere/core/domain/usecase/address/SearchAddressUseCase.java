package br.com.fiap.gastrosphere.core.domain.usecase.address;

import br.com.fiap.gastrosphere.core.domain.entity.Address;
import br.com.fiap.gastrosphere.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.address.AddressInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchAddressUseCase {

    private final AddressInterface addressInterface;

    public List<Address> execute() throws AddressNotFoundException {

        List<Address> addresses = addressInterface.findAll();

        if (addresses.isEmpty()) {
            throw new AddressNotFoundException("Nenhum endereço encontrado");
        }

        return addresses;
    }

}
