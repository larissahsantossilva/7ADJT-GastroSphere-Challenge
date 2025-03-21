package br.com.fiap.gastrosphere.core.domain.output.address;

import br.com.fiap.gastrosphere.core.domain.generic.output.OutputInterface;
import br.com.fiap.gastrosphere.core.domain.generic.output.OutputStatus;
import br.com.fiap.gastrosphere.core.infraestructure.model.Address;

import java.util.List;

public class FindAllAddressOutput implements OutputInterface {
    private List<Address> addressess;
    private OutputStatus outputStatus;

    public FindAllAddressOutput(List<Address> addressess, OutputStatus outputStatus) {
        this.addressess = addressess;
        this.outputStatus = outputStatus;
    }

    @Override
    public Object getBody() {
        return this.addressess;
    }

    @Override
    public OutputStatus getOutputStatus() {
        return null;
    }
}
