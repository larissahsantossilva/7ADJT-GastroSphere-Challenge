package br.com.fiap.gastrosphere.core.infra.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "restaurant_type", schema = "gastrosphere")
public class RestaurantTypeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "lastModifiedAt", nullable = false)
    private LocalDate lastModifiedAt = LocalDate.now();

}
