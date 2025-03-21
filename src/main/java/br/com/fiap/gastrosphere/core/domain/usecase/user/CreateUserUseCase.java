package br.com.fiap.gastrosphere.core.domain.usecase.user;

import br.com.fiap.gastrosphere.core.domain.entity.Address;
import br.com.fiap.gastrosphere.core.domain.entity.User;
import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.usertype.UserTypeNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.user.UserInterface;
import br.com.fiap.gastrosphere.core.domain.gateway.usertype.UserTypeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserInterface userInterface;
    private final UserTypeInterface userTypeInterface;

    public User execute(String name,
                        String email,
                        String login,
                        String password,
                        UUID userTypeId,
                        String document,
                        Address address) throws UserTypeNotFoundException {

        UserType userType = userTypeInterface.findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("Tipo de usuário não encontrado"));

        User user = new User(
                name,
                email,
                login,
                password,
                userType,
                document,
                address,
                LocalDate.now(),
                LocalDate.now()
        );

        return userInterface.save(user);
    }

}
