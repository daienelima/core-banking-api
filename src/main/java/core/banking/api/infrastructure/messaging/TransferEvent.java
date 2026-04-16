package core.banking.api.infrastructure.messaging;

import java.math.BigDecimal;

public record TransferEvent(
    Long fromAccountId,
    Long toAccountId,
    BigDecimal amount
) {}