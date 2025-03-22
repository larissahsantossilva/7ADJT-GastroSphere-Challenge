package br.com.fiap.gastrosphere.core.domain.usecase.usertype;

import br.com.fiap.gastrosphere.core.domain.entity.UserType;
import br.com.fiap.gastrosphere.core.domain.exception.usertype.UserTypeNotFoundException;
import br.com.fiap.gastrosphere.core.domain.gateway.usertype.UserTypeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchUserTypeUseCase {

    private final UserTypeInterface userTypeInterface;

    public List<UserType> execute() throws UserTypeNotFoundException {

        List<UserType> userTypes = userTypeInterface.findAll();

        if (userTypes.isEmpty()) {
            throw new UserTypeNotFoundException("Nenhum tipo de usuário encontrado");
        }

        return userTypes;
    }

}
