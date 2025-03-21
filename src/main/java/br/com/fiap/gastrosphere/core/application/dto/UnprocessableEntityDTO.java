package br.com.fiap.gastrosphere.core.application.dto;

public record UnprocessableEntityDTO(int statusCode, String errorMessage) {

}