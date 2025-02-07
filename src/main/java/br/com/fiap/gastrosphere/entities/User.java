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

    private String document;

    private UUID addressId;

    private String addressNumber;

    private String addressComplement;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;
}
