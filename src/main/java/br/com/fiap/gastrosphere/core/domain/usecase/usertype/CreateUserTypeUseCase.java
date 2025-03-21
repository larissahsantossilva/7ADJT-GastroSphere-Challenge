package br.com.fiap.gastrosphere.core.domain.usecase.usertype;

import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.usertype.UserTypeAlreadyExistsException;
import br.com.fiap.gastrosphere.core.domain.gateway.usertype.UserTypeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class CreateUserTypeUseCase {

    private final UserTypeInterface userTypeInterface;

    public UserType execute(String name) throws UserTypeAlreadyExistsException {

        Optional<UserType> existingUserType = userTypeInterface.findByName(name);

        if (existingUserType.isPresent()) {
            throw new UserTypeAlreadyExistsException("Tipo de usuário já existe");
        }

        UserType userType = new UserType(
                name,
                LocalDate.now(),
                LocalDate.now()
        );

        return userTypeInterface.save(userType);
    }

}
