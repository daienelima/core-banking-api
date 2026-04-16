package core.banking.api.domain.repository;

import core.banking.api.domain.model.Transaction;

public interface TransactionRepository {

    void save(Transaction transaction);
}