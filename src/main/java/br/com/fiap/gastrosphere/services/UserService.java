package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository; }

    public List<User> findAllUsers(int page, int size) {
        int offset = (page - 1) * size;
        return this.userRepository.findAll(size, offset);
    }
    
	public Optional<User> findById(UUID id) {
		return this.userRepository.findById(id);
	}
}
