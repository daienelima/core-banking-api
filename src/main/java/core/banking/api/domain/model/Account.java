package core.banking.api.domain.model;

import core.banking.api.domain.exception.SaldoInsuficienteException;

import java.math.BigDecimal;

public record Account(Long id, String name, BigDecimal balance) {

    public Account debit(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new SaldoInsuficienteException();
        }
        return new Account(id, name, balance.subtract(amount));
    }

    public Account credit(BigDecimal amount) {
        return new Account(id, name, balance.add(amount));
    }
}