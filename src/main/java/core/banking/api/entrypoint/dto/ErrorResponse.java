package core.banking.api.entrypoint.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta de erro")
public record ErrorResponse(
    @Schema(description = "Status HTTP", example = "400")
    int status,
    
    @Schema(description = "Mensagem de erro", example = "Saldo insuficiente")
    String message,
    
    @Schema(description = "Timestamp do erro", example = "2026-04-15T10:30:00")
    LocalDateTime timestamp
) {}

