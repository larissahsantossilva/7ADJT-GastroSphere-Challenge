package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.UserType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import br.com.fiap.gastrosphere.repositories.UserTypeRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserTypeServiceImpl {
    private static final Logger logger = getLogger(UserTypeServiceImpl.class);
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    public UserTypeServiceImpl(UserTypeRepository userTypeRepository,
                              UserRepository userRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    public Page<UserType> findAllUserTypes(int page, int size) {
        return userTypeRepository.findAll(PageRequest.of(page, size));
    }

    public UserType findById(UUID id) {
        uuidValidator(id);
        return userTypeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public UserType createUserType(UserType userType) {
        try {
            return userTypeRepository.save(userType);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_TIPO_USUARIO, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_TIPO_USUARIO);
        }
    }

    public UserType updateUserType(UUID id, UserType userType) {
        UserType existingUserType = userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TIPO_USUARIO_NAO_ENCONTRADO));

        if (userType.getName() != null) existingUserType.setName(userType.getName());

        try {
            return userTypeRepository.save(existingUserType);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_TIPO_USUARIO, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_TIPO_USUARIO);
        }
    }

    public void deleteUserTypeById(UUID id) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TIPO_USUARIO_NAO_ENCONTRADO));

        if (userRepository.existsByUserType_Id(id)) {
            logger.error(TIPO_USUARIO_ASSOCIADO_A_USUARIO);
            throw new UnprocessableEntityException(TIPO_USUARIO_ASSOCIADO_A_USUARIO);
        }

        try {
            userTypeRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_TIPO_USUARIO, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_TIPO_USUARIO);
        }
    }
}
