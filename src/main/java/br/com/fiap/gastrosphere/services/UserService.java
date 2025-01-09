package br.com.fiap.gastrosphere.services;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	private final AddressService addressService;

	public UserService(UserRepository userRepository, AddressService addressService) {
		this.addressService = addressService;
		this.userRepository = userRepository;
	}

	public List<User> findAllUsers(int page, int size) {
		int offset = (page - 1) * size;
		return this.userRepository.findAll(size, offset) ;
	}

	public Optional<User> findById(UUID id) {
			return ofNullable(this.userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado")));
	}

	public Optional<Integer> deleteById(UUID id) {
		Optional<Integer> result = this.userRepository.deleteById(id);
		if(result.isPresent()) {
			if (result.get().equals(0)) {
				throw new ResourceNotFoundException("Usuário não encontrado");
			}
		}
		return result;
	}
}
