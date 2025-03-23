package br.com.fiap.gastrosphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundDTO {
    private String errorMessage;
    private int statusCode;
}
