package br.com.fiap.gastrosphere.dtos;

import br.com.fiap.gastrosphere.entities.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeDTO {

    private String name;

    public UserTypeDTO(UserType userType){
        this.name = userType.getName();
    }
}
