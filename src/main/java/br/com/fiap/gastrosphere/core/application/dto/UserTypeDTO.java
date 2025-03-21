package br.com.fiap.gastrosphere.core.application.dto;

import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeDTO {

    private String name;

    public UserTypeDTO(UserTypeModel userType){
        this.name = userType.getName();
    }
}
