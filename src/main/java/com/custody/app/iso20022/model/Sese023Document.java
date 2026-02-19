package com.custody.app.iso20022.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Document")
@Data
public class Sese023Document {

    @XmlElement(name = "SctiesSttlmTxInstr")
    private SecuritiesSettlementTransactionInstruction instruction;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class SecuritiesSettlementTransactionInstruction {
        @XmlElement(name = "TxId")
        private String transactionId;

        @XmlElement(name = "SttlmTpAndAddtlParams")
        private SettlementTypeAndAdditionalParameters settlementType;

        @XmlElement(name = "TradDtls")
        private TradeDetails tradeDetails;

        @XmlElement(name = "FinInstrmId")
        private FinancialInstrumentIdentification financialInstrumentId;

        @XmlElement(name = "QtyAndAcctDtls")
        private QuantityAndAccountDetails quantityAndAccountDetails;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class SettlementTypeAndAdditionalParameters {
        @XmlElement(name = "SctiesMvmntTp")
        private String securitiesMovementType; // DELI or RECE

        @XmlElement(name = "Pmt")
        private String payment; // APMT or FREE
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class TradeDetails {
        @XmlElement(name = "TradDt")
        private DateTrade tradeDate;

        @XmlElement(name = "SttlmDt")
        private DateTrade settlementDate;

        @XmlAccessorType(XmlAccessType.FIELD)
        @Data
        public static class DateTrade {
            @XmlElement(name = "Dt")
            private DateDetails date;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @Data
        public static class DateDetails {
            @XmlElement(name = "Dt")
            private String dateString; // Simplified for MVP
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class FinancialInstrumentIdentification {
        @XmlElement(name = "ISIN")
        private String isin;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class QuantityAndAccountDetails {
        @XmlElement(name = "SttlmQty")
        private SettlementQuantity settlementQuantity;

        @XmlElement(name = "SfkpgAcct")
        private SafekeepingAccount safekeepingAccount;

        @XmlAccessorType(XmlAccessType.FIELD)
        @Data
        public static class SettlementQuantity {
            @XmlElement(name = "Qty")
            private Quantity quantity;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @Data
        public static class Quantity {
            @XmlElement(name = "Unit")
            private BigDecimal unit;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @Data
        public static class SafekeepingAccount {
            @XmlElement(name = "Id")
            private String id;
        }
    }
}
