package br.com.fiap.gastrosphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IllegalArgumentDTO {
    private int statusCode;
    private String errorMessage;
}