package com.custody.app.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "instructions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instructionId;
    private String isin;
    private String accountId;
    private BigDecimal quantity;
    private String movementType; // RECE, DELI
    private String source; // DEPOSITORY, INTERNAL
    private String status; // PENDING, VALIDATED, REJECTED, MATCHED, SETTLED
    private String matchingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Instruction(String instructionId, String isin, String accountId, BigDecimal quantity, String movementType,
            String source, String status) {
        this.instructionId = instructionId;
        this.isin = isin;
        this.accountId = accountId;
        this.quantity = quantity;
        this.movementType = movementType;
        this.source = source;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
