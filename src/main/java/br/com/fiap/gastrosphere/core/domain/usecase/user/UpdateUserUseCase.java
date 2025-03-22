package br.com.fiap.gastrosphere.core.domain.usecase.user;

import br.com.fiap.gastrosphere.core.domain.entity.Address;
import br.com.fiap.gastrosphere.core.domain.entity.User;
import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.usertype.UserTypeNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.user.UserInterface;
import br.com.fiap.gastrosphere.core.domain.gateway.usertype.UserTypeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserInterface userInterface;
    private final UserTypeInterface userTypeInterface;

    public User execute(UUID userId,
                        String name,
                        String email,
                        String login,
                        String password,
                        UUID userTypeId,
                        String document,
                        Address address) throws UserTypeNotFoundException, UserNotFoundException {

        Optional<User> existingUserOptional = userInterface.findById(userId);
        if (!existingUserOptional.isPresent()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        User existingUser = existingUserOptional.get();

        UserType userType = userTypeInterface.findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("Tipo de usuário não encontrado"));

        existingUser.setName(name);
        existingUser.setEmail(email);
        existingUser.setLogin(login);
        existingUser.setPassword(password);
        existingUser.setUserType(userType);
        existingUser.setDocument(document);
        existingUser.setAddress(address);
        existingUser.setLastModifiedAt(LocalDate.now());

        return userInterface.save(existingUser);
    }

}
