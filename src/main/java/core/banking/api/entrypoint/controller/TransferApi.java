package core.banking.api.entrypoint.controller;


import core.banking.api.entrypoint.dto.ErrorResponse;
import core.banking.api.entrypoint.dto.TransferRequest;
import core.banking.api.entrypoint.dto.TransferResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Transfers", description = "Endpoints para realizar transferências entre contas")
public interface TransferApi {

    @PostMapping
    @Operation(
        summary = "Realizar transferência",
        description = "Realiza uma transferência de saldo entre duas contas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Transferência realizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransferResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validação falhou ou saldo insuficiente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Conta não encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request);
}

