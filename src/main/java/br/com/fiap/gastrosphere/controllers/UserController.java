package br.com.fiap.gastrosphere.controllers;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.gastrosphere.dtos.UserDto;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.services.UserService;

@RestController
@RequestMapping(UserController.V1_USER)
@Tag(name = "UserController", description = "Controller para CRUD de usuários.")
public class UserController {

	public static final String V1_USER = "/api/v1/users";

	private static final Logger logger = getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(
		description = "Busca todos os usuários de forma paginada.",
		summary = "Busca todos os usuários de forma paginada.",
		responses = {
			@ApiResponse(description = "OK", responseCode = "200"),
			@ApiResponse(description = "No content", responseCode = "204")
		}
	)
	@GetMapping
	public ResponseEntity<List<UserDto>> findAllUsers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		logger.info("GET | {} | Iniciado findAllUsers", V1_USER);
		var users = this.userService.findAllUsers(page, size);
		logger.info("GET | {} | Finalizado findAllUsers", V1_USER);
		return ok(users);
	}

	@Operation(
		description = "Busca usuários por id.",
		summary = "Busca usuários por id.",
		responses = {
			@ApiResponse(description = "OK", responseCode = "200"),
			@ApiResponse(description = "Not found", responseCode = "404")
		}
	)
	@GetMapping("/{id}")
	public ResponseEntity<Optional<UserDto>> findUserById(@PathVariable("id") UUID id) {
		logger.info("GET | {} | Iniciado findUserById | id: {}", V1_USER, id);
		var user = userService.findById(id);
		logger.info("GET | {} | Finalizado findUserByUd | id: {}", V1_USER, id);
		return ok(user);
	}

	@Operation(
		description = "Cria usuário.",
		summary = "Cria usuário.",
		responses = {
			@ApiResponse(description = "OK", responseCode = "201")
		}
	)
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody User user) {
		logger.info("POST | {} | Iniciado createUser | User: {}", V1_USER, user.getCpf());
		userService.createUser(user);
		logger.info("POST | {} | Finalizado createUser", V1_USER);
		return status(201).body("Usuário criado com sucesso");
	}

	@Operation(
		description = "Atualiza usuário por id.",
		summary = "Atualiza usuário por id.",
		responses = {
			@ApiResponse(description = "OK", responseCode = "200")
		}
	)
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable("id") UUID id, @RequestBody User user) {
		logger.info("PUT | {} | Iniciado updateUser | id: {}", V1_USER, id);
		userService.updateUser(user, id);
		logger.info("PUT | {} | Finalizado updateUser", V1_USER);
		return ok().build();
	}

	@Operation(
		description = "Exclui usuário por id.",
		summary = "Exclui usuário por id.",
		responses = {
			@ApiResponse(description = "OK", responseCode = "200")
		}
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id) {
		logger.info("DELETE | {} | Iniciado deleteUserById | id: {}", V1_USER, id);
		userService.deleteById(id);
		logger.info("DELETE | {} | Finalizado deleteUserByUd | id: {}", V1_USER, id);
		return noContent().build();
	}

}
