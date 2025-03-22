package br.com.fiap.gastrosphere.core.domain.entity;

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

    private UserType userType;

    private String document;

    private Address address;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;

    public User(String name,
                String email,
                String login,
                String password,
                UserType userType,
                String document,
                Address address,
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
