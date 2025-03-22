package br.com.fiap.gastrosphere.core.domain.usecase.usertype;

import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.usertype.UserTypeNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.usertype.UserTypeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UpdateUserTypeUseCase {

    private final UserTypeInterface userTypeInterface;

    public UserType execute(UUID id, String name) throws UserTypeNotFoundException {

        Optional<UserType> userTypeOptional = userTypeInterface.findById(id);

        if (!userTypeOptional.isPresent()) {
            throw new UserTypeNotFoundException("Tipo de usuário não encontrado");
        }

        UserType userType = userTypeOptional.get();
        userType.setName(name);
        userType.setLastModifiedAt(LocalDate.now());

        return userTypeInterface.save(userType);
    }

}
