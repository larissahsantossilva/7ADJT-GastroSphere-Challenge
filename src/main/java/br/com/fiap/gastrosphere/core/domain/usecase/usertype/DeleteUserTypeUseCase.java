package br.com.fiap.gastrosphere.core.domain.usecase.usertype;

import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.usertype.UserTypeNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.usertype.UserTypeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DeleteUserTypeUseCase {

    private final UserTypeInterface userTypeInterface;

    public void execute(UUID id) throws UserTypeNotFoundException {

        Optional<UserType> userTypeOptional = userTypeInterface.findById(id);

        if (userTypeOptional.isEmpty()) {
            throw new UserTypeNotFoundException("Tipo usuário não encontrado.");
        }

        userTypeInterface.delete(id);
    }

}
