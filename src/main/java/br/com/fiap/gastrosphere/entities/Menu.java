package br.com.fiap.gastrosphere.entities;

import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menus", schema = "gastrosphere")
public class Menu {
	
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = now();

    @Column(nullable = false)
    private LocalDate lastModifiedAt = now();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "menu")
    private List<MenuItem> itemsMenu = new ArrayList<>();
 }
