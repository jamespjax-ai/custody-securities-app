package com.custody.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "positions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountId", "isin"})
})
@Data
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;
    private String isin;
    private BigDecimal quantity;

    public Position(String accountId, String isin, BigDecimal quantity) {
        this.accountId = accountId;
        this.isin = isin;
        this.quantity = quantity;
    }
}
