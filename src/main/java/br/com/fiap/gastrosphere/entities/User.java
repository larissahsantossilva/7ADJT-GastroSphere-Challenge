package br.com.fiap.gastrosphere.entities;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;

    private String name;

    private String email;

    private String login;

    private String password;

    private String userType;

    private String cpf;

    private String addressId;
    
    private Address address;

    private String addressNumber;

    private String addressComplement;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;
}
