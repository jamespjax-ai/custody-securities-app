package com.custody.app.domain.service;

import com.custody.app.adapter.out.PositionRepository;
import com.custody.app.domain.model.Position;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final TransactionService transactionService;

    public PositionService(PositionRepository positionRepository, TransactionService transactionService) {
        this.positionRepository = positionRepository;
        this.transactionService = transactionService;
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

        if ("RECE".equalsIgnoreCase(movementType)) {
            position.setQuantity(position.getQuantity().add(quantity));
        } else if ("DELI".equalsIgnoreCase(movementType)) {
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
}
