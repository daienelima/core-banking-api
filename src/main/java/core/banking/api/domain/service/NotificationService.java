package core.banking.api.domain.service;

import core.banking.api.domain.model.Account;

import java.math.BigDecimal;

public interface NotificationService {
    void notifyTransfer(Account from, Account to, BigDecimal amount);
}