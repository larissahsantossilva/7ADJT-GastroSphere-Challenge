package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.dtos.UserDto;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.AddressRepository;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AddressService {
    private static final Logger logger = getLogger(AddressService.class);
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<Address> findAllAddresses(int page, int size) {
        int offset = (page - 1) * size;
        return this.addressRepository.findAll(size, offset);
    }

    public Optional<Address> findById(UUID id) {
        uuidValidator(id);
        return this.addressRepository.findById(id);
    }

    public Optional<Address> findAddressByZipCode(String zipCode) {
        return this.addressRepository.findByZipCode(zipCode);
    }

    public void createAddress(Address address) {
        Optional<Integer> result;
        try {
            result = this.addressRepository.create(address);
            if (result.isPresent() && result.get() != 1) {
                logger.error(ERRO_AO_CRIAR_ENDERECO);
                throw new UnprocessableEntityException(ERRO_AO_CRIAR_ENDERECO);
            }
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_ENDERECO, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_ENDERECO);
        }
    }

    public void updateAddress(UUID id, Address address) {
        Optional<Integer> result;
        try {
            result = this.addressRepository.updateById(id, address);
            if (result.isPresent() && result.get() != 1) {
                logger.error(ENDERECO_NAO_ENCONTRADO);
                throw new ResourceNotFoundException(ENDERECO_NAO_ENCONTRADO);
            }
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_ENDERECO, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_ENDERECO);
        }
    }

    public void deleteAddressById(UUID id) {
        Optional<Integer> result;
        Optional<UserDto> user = this.userRepository.findByAddressId(id);
        if(user.isPresent()){
            logger.error(ENDERECO_ASSOCIADO_A_USUARIO);
            throw new UnprocessableEntityException(ENDERECO_ASSOCIADO_A_USUARIO);
        }
        try {
            result = this.addressRepository.deleteById(id);
            if (result.isPresent() && result.get() != 1) {
                throw new ResourceNotFoundException(ENDERECO_NAO_ENCONTRADO);
            }
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_ENDERECO, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_ENDERECO);
        }
    }
}
