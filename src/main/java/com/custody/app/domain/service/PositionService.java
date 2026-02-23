package com.custody.app.domain.service;

import com.custody.app.adapter.out.PositionRepository;
import com.custody.app.domain.model.Position;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final TransactionService transactionService;
    private final com.custody.app.adapter.out.TransactionRepository transactionRepository;

    public PositionService(PositionRepository positionRepository,
            TransactionService transactionService,
            com.custody.app.adapter.out.TransactionRepository transactionRepository) {
        this.positionRepository = positionRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Position updatePosition(String accountId, String isin, BigDecimal quantity, String movementType) {
        return updatePositionWithProcess(accountId, isin, quantity, movementType, "SETTLEMENT",
                "AUTO-" + System.currentTimeMillis());
    }

    @Transactional
    public Position updatePositionWithProcess(String accountId, String isin, BigDecimal quantity, String movementType,
            String process, String txId) {
        Optional<Position> existingPosition = positionRepository.findByAccountIdAndIsin(accountId, isin);

        Position position;
        if (existingPosition.isPresent()) {
            position = existingPosition.get();
        } else {
            position = new Position(accountId, isin, BigDecimal.ZERO);
        }

        if ("RECE".equalsIgnoreCase(movementType) || "REC".equalsIgnoreCase(movementType)) {
            position.setQuantity(position.getQuantity().add(quantity));
        } else if ("DELI".equalsIgnoreCase(movementType) || "DEL".equalsIgnoreCase(movementType)) {
            position.setQuantity(position.getQuantity().subtract(quantity));
        }

        // Log the transaction for audit trail
        transactionService.logTransaction(txId, isin, accountId, quantity, movementType, process);

        return positionRepository.save(position);
    }

    public BigDecimal getPositionQuantity(String accountId, String isin) {
        return positionRepository.findByAccountIdAndIsin(accountId, isin)
                .map(Position::getQuantity)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Calculates the position at a specific point in time (Intraday/Digital
     * snapshot).
     * It starts with the current balance and reverses transactions that happened
     * after the target time.
     */
    public BigDecimal getPositionAtTime(String accountId, String isin, LocalDateTime targetTime) {
        BigDecimal currentBalance = getPositionQuantity(accountId, isin);

        // Find all transactions that occurred AFTER the target time
        java.util.List<com.custody.app.domain.model.Transaction> futureTransactions = transactionRepository
                .findByAccountIdAndIsinAndExecutionTimeAfter(accountId, isin, targetTime);

        BigDecimal historicalBalance = currentBalance;

        for (com.custody.app.domain.model.Transaction tx : futureTransactions) {
            if ("RECE".equalsIgnoreCase(tx.getMovementType()) || "REC".equalsIgnoreCase(tx.getMovementType())) {
                // If it was received after target time, it wasn't there yet. Subtract it.
                historicalBalance = historicalBalance.subtract(tx.getQuantity());
            } else if ("DELI".equalsIgnoreCase(tx.getMovementType()) || "DEL".equalsIgnoreCase(tx.getMovementType())) {
                // If it was delivered after target time, it was there then. Add it back.
                historicalBalance = historicalBalance.add(tx.getQuantity());
            }
        }

        return historicalBalance;
    }
}
