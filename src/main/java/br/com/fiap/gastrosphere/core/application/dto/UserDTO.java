package br.com.fiap.gastrosphere.core.application.dto;

import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String name;

    public UserDTO(UserModel user){
        this.name = user.getName();
    }

}
