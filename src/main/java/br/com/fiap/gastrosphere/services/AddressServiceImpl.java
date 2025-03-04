package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.dtos.responses.UserBodyResponse;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.AddressRepository;
import br.com.fiap.gastrosphere.repositories.RestaurantRepository;
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
        Optional<UserBodyResponse> user = this.userRepository.findByAddressId(id);
        Optional<Restaurant> restaurant = this.restaurantRepository.findByAddressId(id);

        if(user.isPresent()){
            logger.error(ENDERECO_ASSOCIADO_A_USUARIO);
            throw new UnprocessableEntityException(ENDERECO_ASSOCIADO_A_USUARIO);
        }
        if(restaurant.isPresent()){
            logger.error(ENDERECO_ASSOCIADO_A_RESTAURANTE);
            throw new UnprocessableEntityException(ENDERECO_ASSOCIADO_A_RESTAURANTE);
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
