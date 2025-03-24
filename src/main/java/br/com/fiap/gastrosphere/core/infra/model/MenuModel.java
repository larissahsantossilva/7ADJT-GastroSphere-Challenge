package br.com.fiap.gastrosphere.core.infra.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.AUTO;
import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menus", schema = "gastrosphere")
public class MenuModel {
	
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = now();

    @Column(nullable = false)
    private LocalDate lastModifiedAt = now();

    @OneToMany(mappedBy = "menu", cascade = ALL, orphanRemoval = true)
    private List<MenuItemModel> itemsMenu = new ArrayList<>();
}
