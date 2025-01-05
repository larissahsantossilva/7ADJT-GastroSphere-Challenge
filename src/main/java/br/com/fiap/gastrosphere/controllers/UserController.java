package br.com.fiap.gastrosphere.controllers;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.services.UserService;

@RestController
@RequestMapping(UserController.V1_USER)
public class UserController {

	public static final String V1_USER = "/api/v1/users";

	private static final Logger logger = getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Exemplo: http://localhost:8080/api/v1/users?page=1&size=10
	@GetMapping
	public ResponseEntity<List<User>> findAllUsers(
			@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "size", defaultValue = "10") int size) {
		logger.info("GET | {} | Iniciado findAllUsers", V1_USER);
		var users = this.userService.findAllUsers(page, size);
		logger.info("GET | {} | Finalizado findAllUsers", V1_USER);
		return ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<User>> findUserById(@PathVariable("id") UUID id) {
		logger.info("GET | {} | Iniciado findUserById | id: {}", V1_USER, id);
		var user = userService.findById(id);
		logger.info("GET | {} | Finalizado findUserByUd | id: {}", V1_USER, id);
		return ok(user);
	}
	
//    @PostMapping
//    public ResponseEntity<String> saveUser(@RequestBody User user) {
//        logger.info("POST | {} | Iniciado saveUser | User: ", V1_USER, user.getCpf());
//        userService.saveUser(user);
//        logger.info("POST | {} | Finalizado saveUser", V1_USER);
//        return status(201).body("Usuário criado com sucesso");
//    }

}
