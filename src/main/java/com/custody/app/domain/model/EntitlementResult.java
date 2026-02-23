package com.custody.app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class EntitlementResult {
    private String accountId;
    private String isin;
    private BigDecimal eligibleQuantity;
    private BigDecimal entitlementAmount;
}
