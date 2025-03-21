package br.com.fiap.gastrosphere.core.application;

import br.com.fiap.gastrosphere.core.infraestructure.repositories.AddressRepository;
import br.com.fiap.gastrosphere.entities.Address;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_200;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.OK;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private static final Logger logger = getLogger(br.com.fiap.gastrosphere.controllers.AddressController.class);
    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Operation(
            description = "Busca todos os endereços de forma paginada.",
            summary = "Busca todos os endereços de forma paginada.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<Address>> findAllAddresses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "zipCode", required = false) String zipCode) {
        if (zipCode != null && !zipCode.isEmpty()) {
            logger.info("GET | {} | Iniciado busca de endereço pelo zipCode | ZipCode: {}", V1_ADDRESS, zipCode);
            Page<Address> addressByZipCode = this.addressService.findAddressByZipCode(zipCode, page, size);
            logger.info("GET | {} | Finalizado busca de endereço pelo zipCode | ZipCode: {}", V1_ADDRESS, zipCode);
            return ok(addressByZipCode);
        } else {
            logger.info("GET | {} | Iniciado busca de endereço por paginação", V1_ADDRESS);
            Page<Address> addresses = addressService.findAllAddresses(page, size);
            logger.info("GET | {} | Finalizado busca de endereço por paginação", V1_ADDRESS);
            return ok(addresses);
        }
    }
}
