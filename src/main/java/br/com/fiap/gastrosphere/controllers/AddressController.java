package br.com.fiap.gastrosphere.controllers;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_204;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToAddress;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.dtos.AddressRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.services.AddressService;

@RestController
@RequestMapping(AddressController.V1_ADDRESS)
@Tag(name = "AddressController", description = "Controller para CRUD de endereços.")
public class AddressController {

    static final String V1_ADDRESS = "/api/v1/addresses";
	private static final Logger logger = getLogger(AddressController.class);
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
        description = "Busca todos os endereços de forma paginada.",
        summary = "Busca todos os endereços de forma paginada.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200)
        }
    )
    @GetMapping
    public ResponseEntity<List<Address>> findAllAddresses(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "zipCode", required = false) String zipCode) {
        if (zipCode != null && !zipCode.isEmpty()) {
            logger.info("GET | {} | Iniciado busca de endereço pelo zipCode | ZipCode: {}", V1_ADDRESS, zipCode);
            Optional<Address> address = this.addressService.findAddressByZipCode(zipCode);
            logger.info("GET | {} | Finalizado busca de endereço pelo zipCode | ZipCode: {}", V1_ADDRESS, zipCode);
            return address.map(value -> ok(List.of(value))).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            logger.info("GET | {} | Iniciado busca de endereço por paginação", V1_ADDRESS);
            var addresses = this.addressService.findAllAddresses(page, size);
            logger.info("GET | {} | Finalizado busca de endereço por paginação", V1_ADDRESS);
            return ok(addresses);
        }
    }

    @Operation(
        description = "Busca endereço por id.",
        summary = "Busca endereço por id.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = NO_CONTENT, responseCode = HTTP_STATUS_CODE_204)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Address>> findAddressById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findAddressById | id: {}", V1_ADDRESS, id);
        var address = addressService.findById(id);
        if(address.isPresent()){
            logger.info("GET | {} | Finalizado findAddressById | id: {}", V1_ADDRESS, id);
            return ok(address);
        }
        logger.info("GET | {} | Finalizado findAddressById No Content | id: {}", V1_ADDRESS, id);
        return status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
        description = "Cria endereço.",
        summary = "Cria endereço.",
        responses = {
            @ApiResponse(description = ENDERECO_CRIADO_COM_SUCESSO, responseCode = HTTP_STATUS_CODE_201),
            @ApiResponse(description = ERRO_AO_CRIAR_ENDERECO, responseCode = HTTP_STATUS_CODE_422),
        }
    )
    @PostMapping
    public ResponseEntity<String> createAddress(@Valid @RequestBody AddressRequest addressDto) {
        logger.info("POST | {} | Iniciado createAddress. ", V1_ADDRESS);

        logger.info("addressDto.country() {}", addressDto.getCountry());
        logger.info("addressDto.state() {}", addressDto.getState());
        logger.info("addressDto.city() {}", addressDto.getCity());
        logger.info("addressDto.zipCode() {}", addressDto.getZipCode());
        logger.info("addressDto.street() {}", addressDto.getStreet());

        Address address = convertToAddress(addressDto);

        addressService.createAddress(address);
        logger.info("POST | {} | Finalizado createAddress ", V1_ADDRESS);
        return status(201).body(ENDERECO_CRIADO_COM_SUCESSO);
    }

    @Operation(
        description = "Atualiza endereço por id.",
        summary = "Atualiza endereço por id.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = ENDERECO_NAO_ENCONTRADO, responseCode = HTTP_STATUS_CODE_404),
            @ApiResponse(description = ERRO_AO_ALTERAR_ENDERECO, responseCode = HTTP_STATUS_CODE_422),
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable("id") UUID id, @Valid @RequestBody AddressRequest addressDto) {
        logger.info("PUT | {} | Iniciado updateAddress | Id: {} | Dados: {}", V1_ADDRESS, id, addressDto);

        Address address = convertToAddress(addressDto);
        addressService.updateAddress(id, address);
        logger.info("PUT | {} | Finalizado updateAddress | Id: {}", V1_ADDRESS, id);
        return ok("Endereço atualizado com sucesso");
    }

    @Operation(
        description = "Exclui endereço por id.",
        summary = "Exclui endereço por id.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = ENDERECO_NAO_ENCONTRADO, responseCode = HTTP_STATUS_CODE_404)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") UUID id) {
    	logger.info("DELETE | {} | Iniciado deleteAddressById | Id: {}", V1_ADDRESS, id);
        addressService.deleteAddressById(id);
        logger.info("DELETE | {} | Finalizado deleteAddressById | Id: {}", V1_ADDRESS, id);
        return ok().build();
    }
}