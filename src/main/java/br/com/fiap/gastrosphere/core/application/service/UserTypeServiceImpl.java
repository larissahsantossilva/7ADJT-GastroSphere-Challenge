package br.com.fiap.gastrosphere.core.application.service;

import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.repository.UserRepository;
import br.com.fiap.gastrosphere.core.infra.repository.UserTypeRepository;
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
public class UserTypeServiceImpl {
    private static final Logger logger = getLogger(UserTypeServiceImpl.class);
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    public UserTypeServiceImpl(UserTypeRepository userTypeRepository,
                              UserRepository userRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    public Page<UserTypeModel> findAllUserTypes(int page, int size) {
        return userTypeRepository.findAll(PageRequest.of(page, size));
    }

    public UserTypeModel findById(UUID id) {
        uuidValidator(id);
        return userTypeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public UserTypeModel createUserType(UserTypeModel userType) {
        try {
            return userTypeRepository.save(userType);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_TIPO_USUARIO, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_TIPO_USUARIO);
        }
    }

    public UserTypeModel updateUserType(UUID id, UserTypeModel userType) {
        UserTypeModel existingUserType = userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TIPO_USUARIO_NAO_ENCONTRADO));

        if (userType.getName() != null) existingUserType.setName(userType.getName());

        existingUserType.setLastModifiedAt(LocalDate.now());

        try {
            return userTypeRepository.save(existingUserType);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_TIPO_USUARIO, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_TIPO_USUARIO);
        }
    }

    public void deleteUserTypeById(UUID id) {
        UserTypeModel userType = userTypeRepository.findById(id)
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
