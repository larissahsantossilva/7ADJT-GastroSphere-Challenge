package br.com.fiap.gastrosphere.services;

import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.matches;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.controllers.UserController;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.dtos.UserDto;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.repositories.UserRepository;

@Service
public class UserService {

	private static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
	private static final Logger logger = getLogger(UserService.class);
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserDto> findAllUsers(int page, int size) {
		int offset = (page - 1) * size;
		return this.userRepository.findAll(size, offset) ;
	}

	public Optional<UserDto> findById(UUID id) {
        uuidValidator(id);
		return this.userRepository.findById(id);
	}

	public void createUser(User user) {
		Optional<Integer> result;
		try {
			result = this.userRepository.create(user);
			if (result.isPresent() && result.get() != 1) {
				logger.error("Erro ao criar usuário");
				throw new UnprocessableEntityException("Erro ao criar usuário");
			}
		} catch (DataAccessException e) {
			logger.error("Erro ao criar usuário", e);
			throw new UnprocessableEntityException("Erro ao criar usuário");
		}
	}

	public void updateUser(User user, UUID id) {
		this.userRepository.update(user, id);
	}

	public void deleteById(UUID id) {
		Optional<Integer> result;
		 result = this.userRepository.deleteById(id);
		 if(result.isPresent() && result.get() != 1) {
			 logger.error("Usuário não encontrado");
			 throw new ResourceNotFoundException("Usuário não encontrado");
		 }
	}

	private void uuidValidator(UUID id) {
	if (!matches(REGEX_UUID, id.toString())) {
			throw new ResourceNotFoundException("ID de usuário inválido");
		}
	}

	public void updatePassword(UUID id, String oldPassword, String newPassword) {
		uuidValidator(id);
	
		UserDto userDto = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
	
		if (!oldPassword.equals(userDto.password())) {
			throw new IllegalArgumentException("Senha antiga incorreta.");
		}
	
		int updatedRows = userRepository.updatePassword(id, newPassword);
		if (updatedRows == 0) {
			throw new RuntimeException("Erro ao atualizar a senha.");
		}
	}

}
