package br.com.fiap.gastrosphere.core.infra.repository;

import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, UUID> {

    Page<AddressModel> findByZipCode(String zipCode, Pageable pageable);
}
