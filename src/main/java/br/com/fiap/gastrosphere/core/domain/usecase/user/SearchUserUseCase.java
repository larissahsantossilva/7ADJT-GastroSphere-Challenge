package br.com.fiap.gastrosphere.core.domain.usecase.user;

import br.com.fiap.gastrosphere.core.domain.entity.User;
import br.com.fiap.gastrosphere.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.user.UserInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchUserUseCase {

    private final UserInterface userInterface;

    public List<User> execute() throws UserNotFoundException {

        List<User> users = userInterface.findAll();

        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum usuário encontrado");
        }

        return users;
    }

}
