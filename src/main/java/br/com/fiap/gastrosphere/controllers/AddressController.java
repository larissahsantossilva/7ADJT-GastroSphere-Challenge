package br.com.fiap.gastrosphere.controllers;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.services.AddressService;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private static final Logger logger = getLogger(AddressController.class);

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // Exemplo: http://localhost:8080/addresses?page=1&size=10
    @GetMapping
    public ResponseEntity<List<Address>> findAllAddresses(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "zipCode", required = false) String zipCode) {

        if (zipCode != null && !zipCode.isEmpty()) {
            logger.info("Buscando endereço pelo zipCode: " + zipCode);
            Optional<Address> address = this.addressService.findAddressByZipCode(zipCode);

            if (address.isPresent()) {
                return ok(List.of(address.get()));  // Retorna o único endereço encontrado em uma lista
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.info("Foi acessado o endpoint /addresses com paginação");
            var addresses = this.addressService.findAllAddresses(page, size);
            return ok(addresses);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Address>> findAddressById(@PathVariable("id") UUID id) {
        logger.info("Iniciando request | findAddressById id: " + id);
        var address = addressService.findById(id);
        return ok(address);
    }

    @PostMapping
    public ResponseEntity<String> createAddress(@RequestBody Address address) {
        logger.info("Iniciando request | createAddress: " + address);
        addressService.createAddress(address);
        return status(201).body("Endereço criado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable("id") UUID id, @RequestBody Address address) {
        logger.info("Iniciando request | updateAddress id: " + id + " com dados: " + address);
        addressService.updateAddress(id, address);
        return ok("Endereço atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") UUID id) {
        logger.info("Iniciando request | deleteAddress id: " + id);
        addressService.deleteAddress(id);
        return status(204).body("Endereço deletado com sucesso");
    }
}
