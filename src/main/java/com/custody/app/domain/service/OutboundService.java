package com.custody.app.domain.service;

import com.custody.app.iso20022.model.Sese023Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.math.BigDecimal;

@Service
public class OutboundService {

    public String generateSettlementInstruction(String transactionId, String isin, String accountId, BigDecimal quantity, String movementType) {
        try {
            Sese023Document document = new Sese023Document();
            Sese023Document.SecuritiesSettlementTransactionInstruction instruction = new Sese023Document.SecuritiesSettlementTransactionInstruction();
            document.setInstruction(instruction);

            instruction.setTransactionId(transactionId);
            
            // Financial Instrument
            var finId = new Sese023Document.FinancialInstrumentIdentification();
            finId.setIsin(isin);
            instruction.setFinancialInstrumentId(finId);

            // Quantity and Account
            var qtyAcct = new Sese023Document.QuantityAndAccountDetails();
            var sq = new Sese023Document.QuantityAndAccountDetails.SettlementQuantity();
            var q = new Sese023Document.QuantityAndAccountDetails.Quantity();
            q.setUnit(quantity);
            sq.setQuantity(q);
            qtyAcct.setSettlementQuantity(sq);
            
            var acct = new Sese023Document.QuantityAndAccountDetails.SafekeepingAccount();
            acct.setId(accountId);
            qtyAcct.setSafekeepingAccount(acct);
            instruction.setQuantityAndAccountDetails(qtyAcct);

            // Movement Type
            var sttlmParams = new Sese023Document.SettlementTypeAndAdditionalParameters();
            sttlmParams.setSecuritiesMovementType(movementType); // DELI or RECE
            sttlmParams.setPayment("APMT"); // Default to Against Payment
            instruction.setSettlementType(sttlmParams);

            // Marshall to XML
            JAXBContext jaxbContext = JAXBContext.newInstance(Sese023Document.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();
            marshaller.marshal(document, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
            return "Error generating XML: " + e.getMessage();
        }
    }
}
