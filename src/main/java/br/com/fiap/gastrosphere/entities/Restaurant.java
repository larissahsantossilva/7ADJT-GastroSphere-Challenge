package br.com.fiap.gastrosphere.entities;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    private UUID id;

    private String name;

    private UUID addressId;

    private String addressNumber;

    private String addressComplement;

    private String restaurantType;

    private String openingHours;

    private UUID ownerId;
}
