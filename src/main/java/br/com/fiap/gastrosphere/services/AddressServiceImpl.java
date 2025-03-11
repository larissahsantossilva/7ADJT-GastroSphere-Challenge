package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.AddressRepository;
import br.com.fiap.gastrosphere.repositories.RestaurantRepository;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AddressServiceImpl {
    private static final Logger logger = getLogger(AddressServiceImpl.class);
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository,
                              RestaurantRepository restaurantRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Page<Address> findAllAddresses(int page, int size) {
        return addressRepository.findAll(PageRequest.of(page, size));
    }

    public Address findById(UUID id) {
        uuidValidator(id);
        return addressRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public Page<Address> findAddressByZipCode(String zipCode, int page, int size) {
        return addressRepository.findByZipCode(zipCode, PageRequest.of(page, size));
    }

    public Address createAddress(Address address) {
        try {
            return addressRepository.save(address);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_ENDERECO, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_ENDERECO);
        }
    }

    public Address updateAddress(UUID id, Address address) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENDERECO_NAO_ENCONTRADO));

        if (address.getCountry() != null) existingAddress.setCountry(address.getCountry());
        if (address.getState() != null) existingAddress.setState(address.getState());
        if (address.getCity() != null) existingAddress.setCity(address.getCity());
        if (address.getZipCode() != null) existingAddress.setZipCode(address.getZipCode());
        if (address.getStreet() != null) existingAddress.setStreet(address.getStreet());
        if (address.getNumber() != null) existingAddress.setNumber(address.getNumber());
        if (address.getComplement() != null) existingAddress.setComplement(address.getComplement());

        existingAddress.setLastModifiedAt(LocalDate.now());

        try {
            return addressRepository.save(existingAddress);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_ENDERECO, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_ENDERECO);
        }
    }

    public void deleteAddressById(UUID id) {
        if (userRepository.findByAddressId(id).isPresent()) {
            logger.error(ENDERECO_ASSOCIADO_A_USUARIO);
            throw new UnprocessableEntityException(ENDERECO_ASSOCIADO_A_USUARIO);
        }
        if (restaurantRepository.findByAddressId(id).isPresent()) {
            logger.error(ENDERECO_ASSOCIADO_A_RESTAURANTE);
        }
        try {
            addressRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_ENDERECO, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_ENDERECO);
        }
    }

}
