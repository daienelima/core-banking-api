package core.banking.api.entrypoint.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resposta de uma transferência realizada com sucesso")
public record TransferResponse(
    @Schema(description = "ID da conta de origem", example = "1")
    Long fromAccountId,
    
    @Schema(description = "ID da conta de destino", example = "2")
    Long toAccountId,
    
    @Schema(description = "Valor transferido", example = "100.00")
    BigDecimal amount,
    
    @Schema(description = "Data e hora da transferência", example = "2026-04-15T10:30:00")
    LocalDateTime transferredAt,
    
    @Schema(description = "Mensagem de confirmação", example = "Transferência realizada com sucesso")
    String message
) {}

