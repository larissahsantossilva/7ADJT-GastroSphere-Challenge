package br.com.fiap.gastrosphere.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantType {
    private UUID id;
    private String name;
    private LocalDate createdAt = LocalDate.now();
    private LocalDate lastModifiedAt = LocalDate.now();
}
