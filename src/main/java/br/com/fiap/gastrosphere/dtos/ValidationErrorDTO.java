package br.com.fiap.gastrosphere.dtos;

import java.util.List;

public record ValidationErrorDTO(List<String> errors, int status) {

}
