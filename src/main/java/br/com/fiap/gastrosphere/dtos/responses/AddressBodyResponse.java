package br.com.fiap.gastrosphere.dtos.responses;

import br.com.fiap.gastrosphere.entities.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressBodyResponse {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String number;
    private String complement;

    public AddressBodyResponse(Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
        this.country = address.getCountry();
        this.complement = address.getComplement() != null ? address.getComplement() : null;
    }

}
