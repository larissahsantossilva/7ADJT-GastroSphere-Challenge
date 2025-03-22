package br.com.fiap.gastrosphere.core.domain.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Address {

    private UUID id;

    private String country;

    private String state;

    private String city;

    private String zipCode;

    private String street;

    private String number;

    private String complement;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;

    public Address(String country,
                   String state,
                   String city,
                   String zipCode,
                   String street,
                   String number,
                   String complement,
                   LocalDate createdAt,
                   LocalDate lastModifiedAt) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

}
