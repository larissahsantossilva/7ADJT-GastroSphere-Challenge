package br.com.fiap.gastrosphere.core.domain.usecase.address;

import br.com.fiap.gastrosphere.core.domain.entity.Address;
import br.com.fiap.gastrosphere.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.address.AddressInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DeleteAddressUseCase {

    private final AddressInterface addressInterface;

    public void execute(UUID id) throws AddressNotFoundException {

        Optional<Address> addressOptional = addressInterface.findById(id);

        if (addressOptional.isEmpty()) {
            throw new AddressNotFoundException("Endereço não encontrado.");
        }

        addressInterface.delete(id);
    }

}
