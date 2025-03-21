package br.com.fiap.gastrosphere.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserType {

    private UUID id;

    private String name;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;

    public UserType(String name,
                    LocalDate createdAt,
                    LocalDate lastModifiedAt) {
        this.name = name;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

}
