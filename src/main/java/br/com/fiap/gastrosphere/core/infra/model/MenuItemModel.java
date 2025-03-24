package br.com.fiap.gastrosphere.core.infra.model;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;
import static java.time.LocalDate.now;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu_items", schema = "gastrosphere")
public class MenuItemModel {

	@Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuModel menu;
    
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String image;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = now();

    @Column(nullable = false)
    private LocalDate lastModifiedAt = now();
}
