package br.com.fiap.gastrosphere.core.domain.gateway.address;

import br.com.fiap.gastrosphere.core.domain.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressInterface {

    List<Address> findAll();

    Address save(Address address);

    boolean existsByZipCode(String zipCode);

    Optional<Address> findById(UUID id);

    void delete(UUID id);

}
