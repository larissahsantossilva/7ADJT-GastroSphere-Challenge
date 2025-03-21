package br.com.fiap.gastrosphere.core.domain.entity;

import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private UUID id;

    private String name;

    private String email;

    private String login;

    private String password;

    private UserTypeModel userType;

    private String document;

    private AddressModel address;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;

    public User(String name,
                String email,
                String login,
                String password,
                UserTypeModel userType,
                String document,
                AddressModel address,
                LocalDate createdAt,
                LocalDate lastModifiedAt) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.userType = userType;
        this.document = document;
        this.address = address;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

}
