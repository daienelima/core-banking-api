package core.banking.api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(Long id, Long fromAccountId, Long toAccountId, BigDecimal amount, LocalDateTime createdAt) {

    public Transaction(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        this(null, fromAccountId, toAccountId, amount, LocalDateTime.now());
    }
}