package core.banking.api.entrypoint.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Requisição para realizar uma transferência entre contas")
public record TransferRequest(
    @NotNull(message = "ID da conta de origem é obrigatório")
    @Schema(description = "ID da conta de origem", example = "1")
    Long fromAccountId,
    
    @NotNull(message = "ID da conta de destino é obrigatório")
    @Schema(description = "ID da conta de destino", example = "2")
    Long toAccountId,
    
    @NotNull(message = "Valor da transferência é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    @Schema(description = "Valor a ser transferido", example = "100.00")
    BigDecimal amount
) {}