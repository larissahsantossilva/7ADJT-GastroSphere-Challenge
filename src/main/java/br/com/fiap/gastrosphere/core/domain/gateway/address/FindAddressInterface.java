package br.com.fiap.gastrosphere.core.domain.gateway.address;

import br.com.fiap.gastrosphere.core.infraestructure.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAddressInterface {
    Page<Address> findAll(Pageable pageable);
}
