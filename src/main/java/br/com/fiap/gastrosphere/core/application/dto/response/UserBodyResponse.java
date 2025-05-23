package br.com.fiap.gastrosphere.core.application.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import br.com.fiap.gastrosphere.core.application.dto.AddressDTO;
import br.com.fiap.gastrosphere.core.application.dto.UserTypeDTO;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserBodyResponse {

	private UUID id;
	private String name;
	private String email;
	private String login;
	private UserTypeDTO userType;
	private String document;
	private LocalDate createdAt;
	private LocalDate lastModifiedAt;
	private AddressDTO address;

	public UserBodyResponse(UserModel user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.login = user.getLogin();
		this.userType = user.getUserType() != null ? new UserTypeDTO(user.getUserType()) : null;
		this.document = user.getDocument();
		this.createdAt = user.getCreatedAt();
		this.lastModifiedAt = user.getLastModifiedAt();
		this.address = user.getAddress() != null ? new AddressDTO(user.getAddress()) : null;
	}

}
