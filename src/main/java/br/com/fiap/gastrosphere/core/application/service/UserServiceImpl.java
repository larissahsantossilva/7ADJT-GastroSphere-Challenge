package br.com.fiap.gastrosphere.core.application.service;

import br.com.fiap.gastrosphere.core.infra.model.UserModel;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserServiceImpl {
	private static final Logger logger = getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final UserTypeRepository userTypeRepository;

	public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
		this.userRepository = userRepository;
		this.userTypeRepository = userTypeRepository;
	}

	public Page<UserModel> findAllUsers(int page, int size) {
		return userRepository.findAll(PageRequest.of(page, size));
	}

	public UserModel findById(UUID id) {
        uuidValidator(id);
		return userRepository.findById(id).orElseThrow(() ->
				new ResourceNotFoundException(ID_NAO_ENCONTRADO));
	}

	public UserModel createUser(UserModel user) {
		if (user.getUserType() != null) {
			UserTypeModel userType = userTypeRepository.findByName(user.getUserType().getName());
			if (userType == null) {
				throw new UnprocessableEntityException(TIPO_USUARIO_NAO_ENCONTRADO);
			}
			user.setUserType(userType);
		}

		try {
			return userRepository.save(user);
		} catch (DataAccessException e) {
			logger.error(ERRO_AO_CRIAR_USUARIO, e);
			throw new UnprocessableEntityException(ERRO_AO_CRIAR_USUARIO);
		}
	}

	public UserModel updateUser(UserModel user, UUID id) {
		UserModel existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO));

		if (user.getUserType() != null) {
			UserTypeModel userType = userTypeRepository.findByName(user.getUserType().getName());
			if (userType == null) {
				throw new UnprocessableEntityException(TIPO_USUARIO_NAO_ENCONTRADO);
			}
			existingUser.setUserType(userType);
		}

		if (user.getName() != null) existingUser.setName(user.getName());
		if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
		if (user.getLogin() != null) existingUser.setLogin(user.getLogin());
		if (user.getPassword() != null) existingUser.setPassword(user.getPassword());
		if (user.getDocument() != null) existingUser.setDocument(user.getDocument());
		if (user.getAddress() != null) existingUser.setAddress(user.getAddress());

		existingUser.setLastModifiedAt(LocalDate.now());

		try {
			return userRepository.save(existingUser);
		} catch (DataAccessException e) {
			logger.error(ERRO_AO_ALTERAR_USUARIO, e);
			throw new UnprocessableEntityException(ERRO_AO_ALTERAR_USUARIO);
		}
	}

	@Transactional
	public void updatePassword(UUID id, String oldPassword, String newPassword) {
		uuidValidator(id);
		UserModel user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO));

		if (!oldPassword.equals(user.getPassword())) {
			logger.error(SENHA_ANTIGA_INCORRETA);
			throw new IllegalArgumentException(SENHA_ANTIGA_INCORRETA);
		}
		if (newPassword.equals(user.getPassword())) {
			logger.error(SENHA_NOVA_DEVE_SER_DIFERENTE);
			throw new IllegalArgumentException(SENHA_NOVA_DEVE_SER_DIFERENTE);
		}

		int result = userRepository.updatePassword(id, oldPassword, newPassword, LocalDate.now(ZoneId.of("America/Sao_Paulo")));

		if (result == 0) {
			logger.error(ERRO_AO_ATUALIZAR_SENHA);
			throw new UnprocessableEntityException(ERRO_AO_ATUALIZAR_SENHA);
		}
	}

	public void deleteUserById(UUID id) {
		try {
			userRepository.deleteById(id);
		} catch (DataAccessException e) {
			 logger.error(ERRO_AO_DELETAR_USUARIO, e);
			 throw new UnprocessableEntityException(ERRO_AO_DELETAR_USUARIO);
		 }
	}

}
