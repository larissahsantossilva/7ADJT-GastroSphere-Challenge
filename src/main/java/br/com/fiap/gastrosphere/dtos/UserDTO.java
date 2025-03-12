package br.com.fiap.gastrosphere.dtos;

import br.com.fiap.gastrosphere.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String name;

    public UserDTO(User user){
        this.name = user.getName();
    }

}
