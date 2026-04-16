package core.banking.api.domain.repository;

import core.banking.api.domain.model.Account;

public interface AccountRepository {

    Account findById(Long id);

    Account findByIdForUpdate(Long id);

    void save(Account account);
}