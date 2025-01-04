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
@RequestMapping(AddressController.V1_ADDRESS)
public class AddressController {

    static final String V1_ADDRESS = "/api/v1/addresses";

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
            logger.info("GET | {} | Iniciado busca de endereço pelo zipCode | ZipCode: {}", V1_ADDRESS, zipCode);
            Optional<Address> address = this.addressService.findAddressByZipCode(zipCode);
            logger.info("GET | {} | Finalizado busca de endereço pelo zipCode | ZipCode: {}", V1_ADDRESS, zipCode);
            if (address.isPresent()) {
                return ok(List.of(address.get()));  // Retorna o único endereço encontrado em uma lista
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.info("GET | {} | Iniciado busca de endereço por paginação", V1_ADDRESS);
            var addresses = this.addressService.findAllAddresses(page, size);
            logger.info("GET | {} | Finalizado busca de endereço por paginação", V1_ADDRESS);
            return ok(addresses);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Address>> findAddressById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findAddressById | id: {}", V1_ADDRESS, id);
        var address = addressService.findById(id);
        logger.info("GET | {} | Finalizado findAddressById | id: {}", V1_ADDRESS, id);
        return ok(address);
    }

    @PostMapping
    public ResponseEntity<String> createAddress(@RequestBody Address address) {
        logger.info("POST | {} | Iniciado createAddress ", V1_ADDRESS);
        addressService.createAddress(address);
        logger.info("POST | {} | Finalizado createAddress ", V1_ADDRESS);
        return status(201).body("Endereço criado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable("id") UUID id, @RequestBody Address address) {
        logger.info("PUT | {} | Iniciado updateAddress | Id: {} | Dados: {}", V1_ADDRESS, id, address);
        addressService.updateAddress(id, address);
        logger.info("PUT | {} | Finalizado updateAddress | Id: {}", V1_ADDRESS, id);
        return ok("Endereço atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") UUID id) {
    	logger.info("DELETE | {} | Iniciado deleteAddress | Id: {} | Dados: {}", V1_ADDRESS, id);
        addressService.deleteAddress(id);
        logger.info("DELETE | {} | Finalizado deleteAddress | Id: {} | Dados: {}", V1_ADDRESS, id);
        return status(204).body("Endereço deletado com sucesso");
    }
}
