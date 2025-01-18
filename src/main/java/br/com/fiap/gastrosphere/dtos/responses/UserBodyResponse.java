package br.com.fiap.gastrosphere.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

import br.com.fiap.gastrosphere.entities.Address;

public record UserBodyResponse(
		UUID id,
		String name,
		String email,
		String login,
		String password,
		String userType,
		String document,
		String addressNumber,
		String addressComplement, 
		LocalDate createdAt, 
		LocalDate lastModifiedAt,
		Address address
		) {
}
