package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService; }

    // Exemplo: http://localhost:8080/users?page=1&size=10
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ){
        logger.info("Foi acessado o endpoint /users");
        var users = this.userService.findAllUsers(page,size);
        return ResponseEntity.ok(users);
    };

}
