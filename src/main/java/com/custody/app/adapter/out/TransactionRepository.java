package com.custody.app.adapter.out;

import com.custody.app.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(String accountId);

    List<Transaction> findByIsin(String isin);

    List<Transaction> findByAccountIdAndIsin(String accountId, String isin);

    List<Transaction> findByAccountIdAndIsinAndExecutionTimeAfter(String accountId, String isin,
            java.time.LocalDateTime time);
}
