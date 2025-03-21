package br.com.fiap.gastrosphere.core.application.dto;

import java.util.List;

public record ValidationErrorDTO(List<String> errors, int status) {

}
