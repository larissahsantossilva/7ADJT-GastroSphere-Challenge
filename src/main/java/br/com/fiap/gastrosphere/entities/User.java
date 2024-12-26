package br.com.fiap.gastrosphere.entities;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

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

    private String addressNumber;

    private String addressComplement;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;
}
