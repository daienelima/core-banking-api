package core.banking.api.infrastructure.persistence.repository;

import core.banking.api.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTransactionRepository 
        extends JpaRepository<TransactionEntity, Long> {
}