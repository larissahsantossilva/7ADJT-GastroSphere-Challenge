package br.com.fiap.gastrosphere.entities;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private UUID id;

    private String country;

    private String state;

    private String city;

    private String zipCode;

    private String street;

}
