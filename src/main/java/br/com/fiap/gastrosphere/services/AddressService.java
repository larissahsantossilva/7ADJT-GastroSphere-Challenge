package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> findAllAddresses(int page, int size) {
        int offset = (page - 1) * size;
        return this.addressRepository.findAll(size, offset);
    }

    public Optional<Address> findById(UUID id) {
        return this.addressRepository.findById(id);
    }

    public Optional<Address> findAddressByZipCode(String zipCode) {
        return this.addressRepository.findByZipCode(zipCode);
    }

    public void createAddress(Address address) {
        var create = this.addressRepository.create(address);
        if (create == null) {
            throw new IllegalStateException("Erro ao salvar endereço " + address.getCity());
        }
    }

    public void updateAddress(UUID id, Address address) {
        var update = this.addressRepository.update(id, address);
        if (update == 0) {
            throw new RuntimeException("Endereço não encontrado");
        }
    }

    public void deleteAddress(UUID id) {
        var delete = this.addressRepository.delete(id);
        if (delete == 0) {
            throw new RuntimeException("Endereço não encontrado");
        }
    }
}
