package core.banking.api.application.usecase;

import core.banking.api.domain.service.NotificationService;
import core.banking.api.domain.model.Account;
import core.banking.api.domain.model.Transaction;
import core.banking.api.domain.repository.AccountRepository;
import core.banking.api.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public TransferUseCase(AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void execute(Long fromId, Long toId, BigDecimal amount) {

        Account from = accountRepository.findByIdForUpdate(fromId);
        Account to = accountRepository.findByIdForUpdate(toId);

        from.debit(amount);
        to.credit(amount);

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction transaction = new Transaction(fromId, toId, amount);
        transactionRepository.save(transaction);

        notificationService.notifyTransfer(from, to, amount);
    }
}