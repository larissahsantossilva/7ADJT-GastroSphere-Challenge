package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {

    List<Address> findAll(int size, int offset);

    Optional<Address> findById(UUID id);

    Optional<Address> findByZipCode(String zipCode);

    Optional<Integer> create(Address address);

    Optional<Integer> updateById(UUID id, Address address);

    Optional<Integer> deleteById(UUID id);
}
