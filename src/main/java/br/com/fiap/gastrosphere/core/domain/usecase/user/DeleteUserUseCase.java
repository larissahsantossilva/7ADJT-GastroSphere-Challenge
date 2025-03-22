package br.com.fiap.gastrosphere.core.domain.usecase.user;

import br.com.fiap.gastrosphere.core.domain.entity.User;
import br.com.fiap.gastrosphere.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.user.UserInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserInterface userInterface;

    public void execute(UUID id) throws UserNotFoundException {

        Optional<User> userOptional = userInterface.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        userInterface.delete(id);
    }

}
