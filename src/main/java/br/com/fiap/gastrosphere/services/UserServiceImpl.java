package br.com.fiap.gastrosphere.services;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.dtos.responses.UserBodyResponse;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.repositories.UserRepository;

@Service
public class UserServiceImpl {
	private static final Logger logger = getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserBodyResponse> findAllUsers(int page, int size) {
		int offset = (page - 1) * size;
		logger.info("size {}, offset {}", size, offset);
		return this.userRepository.findAll(size, offset);
	}

	public Optional<UserBodyResponse> findById(UUID id) {
        uuidValidator(id);
		return this.userRepository.findById(id);
	}

	public Optional<UserBodyResponse> findByAddressId(UUID id) {
		uuidValidator(id);
		return this.userRepository.findByAddressId(id);
	}

	public void createUser(User user) {
		Optional<Integer> result;
		try {
			result = this.userRepository.create(user);
			if (result.isPresent() && result.get() != 1) {
				logger.error(ERRO_AO_CRIAR_USUARIO);
				throw new UnprocessableEntityException(ERRO_AO_CRIAR_USUARIO);
			}
		} catch (DataAccessException e) {
			logger.error(ERRO_AO_CRIAR_USUARIO, e);
			throw new UnprocessableEntityException(ERRO_AO_CRIAR_USUARIO);
		}
	}

	public void updateUser(User user, UUID id) {
		Optional<Integer> result;
		try {
			result = this.userRepository.update(user, id);
			if (result.isPresent() && result.get() != 1) {
				logger.error(USUARIO_NAO_ENCONTRADO);
				throw new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO);
			}
		} catch (DataAccessException e) {
			logger.error(ERRO_AO_ALTERAR_USUARIO, e);
			throw new UnprocessableEntityException(ERRO_AO_ALTERAR_USUARIO);
		}
	}

	public void updatePassword(UUID id, String oldPassword, String newPassword) {
		uuidValidator(id);
		UserBodyResponse userDto = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO));
		if (!oldPassword.equals(userDto.password())) {
			logger.error(SENHA_ANTIGA_INCORRETA);
			throw new IllegalArgumentException(SENHA_ANTIGA_INCORRETA);
		}
		if (newPassword.equals(userDto.password())) {
			logger.error(SENHA_NOVA_DEVE_SER_DIFERENTE);
			throw new IllegalArgumentException(SENHA_NOVA_DEVE_SER_DIFERENTE);
		}
		int result = userRepository.updatePassword(id, newPassword);
		if (result == 0) {
			logger.error(ERRO_AO_ATUALIZAR_SENHA);
			throw new UnprocessableEntityException(ERRO_AO_ATUALIZAR_SENHA);
		}
	}

	public void deleteUserById(UUID id) {
		Optional<Integer> result;
		try {
			result = this.userRepository.deleteById(id);
			if (result.isPresent() && result.get() != 1) {
				logger.error(USUARIO_NAO_ENCONTRADO);
				throw new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO);
			}
		} catch (DataAccessException e) {
			 logger.error(ERRO_AO_DELETAR_USUARIO, e);
			 throw new UnprocessableEntityException(ERRO_AO_DELETAR_USUARIO);
		 }
	}
}
