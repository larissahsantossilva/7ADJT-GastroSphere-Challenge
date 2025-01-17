package br.com.fiap.gastrosphere.dtos;

public record UnprocessableEntityDTO(int statusCode, String errorMessage) {

}