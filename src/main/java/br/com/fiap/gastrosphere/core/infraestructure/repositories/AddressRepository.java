package br.com.fiap.gastrosphere.core.infraestructure.repositories;

import br.com.fiap.gastrosphere.core.infraestructure.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    Page<Address> findByZipCode(String zipCode, Pageable pageable);
}
