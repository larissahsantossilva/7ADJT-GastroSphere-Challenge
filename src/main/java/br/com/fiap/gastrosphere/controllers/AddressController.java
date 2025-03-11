package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.AddressBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.AddressServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToAddress;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(AddressController.V1_ADDRESS)
@Tag(name = "AddressController", description = "Controller para CRUD de endereços.")
public class AddressController {

    static final String V1_ADDRESS = "/api/v1/addresses";
	private static final Logger logger = getLogger(AddressController.class);
    private final AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
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


    @Operation(
        description = "Busca endereço por id.",
        summary = "Busca endereço por id.",
        responses = {
            @ApiResponse(
                description = OK,
                responseCode = HTTP_STATUS_CODE_200,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))
            ),
            @ApiResponse(
                description = NOT_FOUND,
                responseCode = HTTP_STATUS_CODE_404,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Address> findAddressById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findAddressById | id: {}", V1_ADDRESS, id);
        var address = addressService.findById(id);
        if (address != null) {
            logger.info("GET | {} | Finalizado findAddressById | id: {}", V1_ADDRESS, id);
            return ok(address);
        }
        logger.info("GET | {} | Finalizado findAddressById Not Found | id: {}", V1_ADDRESS, id);
        return status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(
        description = "Cria endereço.",
        summary = "Cria endereço.",
        responses = {
            @ApiResponse(
                description = ENDERECO_CRIADO_COM_SUCESSO,
                responseCode = HTTP_STATUS_CODE_201,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                description = ERRO_AO_CRIAR_ENDERECO,
                responseCode = HTTP_STATUS_CODE_422,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<UUID> createAddress(@Valid @RequestBody AddressBodyRequest addressDto) {
        logger.info("POST | {} | Iniciado createAddress. ", V1_ADDRESS);
        Address address = addressService.createAddress(convertToAddress(addressDto));
        logger.info("POST | {} | Finalizado createAddress ", V1_ADDRESS);
        return status(201).body(address.getId());
    }


    @Operation(
        description = "Atualiza endereço por id.",
        summary = "Atualiza endereço por id.",
        responses = {
            @ApiResponse(
                description = OK,
                responseCode = HTTP_STATUS_CODE_200,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                description = ENDERECO_NAO_ENCONTRADO,
                responseCode = HTTP_STATUS_CODE_404,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                description = ERRO_AO_ALTERAR_ENDERECO,
                responseCode = HTTP_STATUS_CODE_422,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable("id") UUID id, @Valid @RequestBody AddressBodyRequest addressDto) {
        logger.info("PUT | {} | Iniciado updateAddress | Id: {} | Dados: {}", V1_ADDRESS, id, addressDto);
        addressService.updateAddress(id, convertToAddress(addressDto));
        logger.info("PUT | {} | Finalizado updateAddress | Id: {}", V1_ADDRESS, id);
        return ok("Endereço atualizado com sucesso");
    }


    @Operation(
        description = "Exclui endereço por id.",
        summary = "Exclui endereço por id.",
        responses = {
            @ApiResponse(
                description = NO_CONTENT,
                responseCode = HTTP_STATUS_CODE_204,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                description = ENDERECO_NAO_ENCONTRADO,
                responseCode = HTTP_STATUS_CODE_404,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") UUID id) {
    	logger.info("DELETE | {} | Iniciado deleteAddressById | Id: {}", V1_ADDRESS, id);
        try {
            addressService.deleteAddressById(id);
            logger.info("DELETE | {} | Endereço deletado com sucesso | Id: {}", V1_ADDRESS, id);
            return ResponseEntity.noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar endereço | Id: {} | Erro: {}", V1_ADDRESS, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
        }
    }

}