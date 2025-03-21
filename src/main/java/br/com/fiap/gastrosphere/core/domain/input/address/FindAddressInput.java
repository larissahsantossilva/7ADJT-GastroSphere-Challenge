package br.com.fiap.gastrosphere.core.domain.input.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class FindAddressInput {
    private int page;
    private int size;
}
