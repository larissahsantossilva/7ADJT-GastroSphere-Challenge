package br.com.fiap.gastrosphere.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnprocessableEntityDTO {
    private int statusCode;
    private String errorMessage;
}
