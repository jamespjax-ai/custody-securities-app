package com.custody.app.domain.service;

import com.custody.app.adapter.out.TransactionRepository;
import com.custody.app.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction logTransaction(String transactionId, String isin, String accountId, BigDecimal quantity,
            String movementType, String businessProcess) {
        Transaction transaction = new Transaction(transactionId, isin, accountId, quantity, movementType,
                businessProcess);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
