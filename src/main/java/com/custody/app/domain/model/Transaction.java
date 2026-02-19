package com.custody.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String isin;
    private String accountId;
    private BigDecimal quantity;
    private String movementType; // RECE, DELI
    private String businessProcess; // SETTLEMENT, CORPORATE_ACTION, INCOME
    private LocalDateTime executionTime;

    public Transaction(String transactionId, String isin, String accountId, BigDecimal quantity, String movementType,
            String businessProcess) {
        this.transactionId = transactionId;
        this.isin = isin;
        this.accountId = accountId;
        this.quantity = quantity;
        this.movementType = movementType;
        this.businessProcess = businessProcess;
        this.executionTime = LocalDateTime.now();
    }
}
