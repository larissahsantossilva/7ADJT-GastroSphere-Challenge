package br.com.fiap.gastrosphere.core.domain.usecases;

import br.com.fiap.gastrosphere.core.domain.gateway.address.FindAddressInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputStatus;
import br.com.fiap.gastrosphere.core.domain.input.address.FindAddressInput;
import br.com.fiap.gastrosphere.core.infraestructure.model.Address;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Getter
@RequiredArgsConstructor
public class FindAllAddressesUseCase {
    private final FindAddressInterface findAddressInterface;
    private OutputInterface findAddressOutput;

    public void execute(FindAddressInput findAddressInput) {

        try {
            Page<Address> addressess = findAddressInterface.findAll(PageRequest.of(findAddressInput.getPage(), findAddressInput.getSize()));

            this.findAddressOutput = new IdentificaClienteOutput(
                    addressess,
                    new OutputStatus(200, "Ok", "Address found")
            );
        } catch (Exception e) {
            new OutputError(
                    e.getMessage(),
                    new OutputStatus(500, "Internal Server Error", "Erro no servidor")
            );
        }
    }
}
