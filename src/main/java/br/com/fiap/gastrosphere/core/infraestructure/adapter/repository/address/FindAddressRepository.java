package br.com.fiap.gastrosphere.core.infraestructure.adapter.repository.address;

import br.com.fiap.gastrosphere.core.domain.gateway.address.FindAddressInterface;
import br.com.fiap.gastrosphere.core.infraestructure.model.Address;
import br.com.fiap.gastrosphere.core.infraestructure.repositories.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAddressRepository  implements FindAddressInterface {
    private final AddressRepository addressRepository;

    public FindAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Page<Address> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }
}
