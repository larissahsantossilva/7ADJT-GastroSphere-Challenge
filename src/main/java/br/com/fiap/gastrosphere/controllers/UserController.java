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
@RequestMapping("/api/v1/users")
public class UserController {

	private static final Logger logger = getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Exemplo: http://localhost:8080/users?page=1&size=10
	@GetMapping
	public ResponseEntity<List<User>> findAllUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
		logger.info("Foi acessado o endpoint /users");
		var users = this.userService.findAllUsers(page, size);
		return ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<User>> findUserById(@PathVariable("id") UUID id) {
		logger.info("Iniciando request | findUserById id: " + id);
		var user = userService.findById(id);
		return ok(user);
	}

}
