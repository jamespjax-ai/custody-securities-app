package com.custody.app.domain.service;

import com.custody.app.adapter.out.CorporateActionRepository;
import com.custody.app.domain.model.CorporateActionEvent;
import com.custody.app.domain.model.Position;
import com.custody.app.adapter.out.PositionRepository;
import com.custody.app.domain.model.EntitlementResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorporateActionService {

    private final CorporateActionRepository corporateActionRepository;
    private final PositionService positionService;
    private final PositionRepository positionRepository;

    public CorporateActionService(CorporateActionRepository corporateActionRepository,
            PositionService positionService,
            PositionRepository positionRepository) {
        this.corporateActionRepository = corporateActionRepository;
        this.positionService = positionService;
        this.positionRepository = positionRepository;
    }

    public CorporateActionEvent announceEvent(CorporateActionEvent event) {
        event.setStatus("ANNOUNCED");
        return corporateActionRepository.save(event);
    }

    public List<CorporateActionEvent> getAllEvents() {
        return corporateActionRepository.findAll();
    }

    @Transactional
    public void processBonusIssue(String eventId, String isin, BigDecimal ratio) {
        CorporateActionEvent event = corporateActionRepository.findAll().stream()
                .filter(e -> e.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if ("COMPLETED".equals(event.getStatus()))
            return;

        // Apply bonus to all accounts holding this ISIN
        List<Position> affectedPositions = positionRepository.findByIsin(isin);
        for (Position pos : affectedPositions) {
            BigDecimal bonusQty = pos.getQuantity().multiply(ratio);
            if (bonusQty.compareTo(BigDecimal.ZERO) > 0) {
                positionService.updatePositionWithProcess(
                        pos.getAccountId(),
                        isin,
                        bonusQty,
                        "RECE",
                        "CORPORATE_ACTION",
                        "CA-BONUS-" + eventId);
            }
        }

        event.setStatus("COMPLETED");
        corporateActionRepository.save(event);
    }

    @Transactional
    public void processDividend(String eventId, BigDecimal ratePerShare) {
        CorporateActionEvent event = corporateActionRepository.findAll().stream()
                .filter(e -> e.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if ("COMPLETED".equals(event.getStatus()))
            return;

        // Digital Logic: Calculate entitlement based on precise Record Date snapshot
        List<Position> allActiveHolders = positionRepository.findByIsin(event.getIsin());
        for (Position pos : allActiveHolders) {
            BigDecimal entitledQty = positionService.getPositionAtTime(
                    pos.getAccountId(),
                    event.getIsin(),
                    event.getRecordDate());

            if (entitledQty.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal cashAmount = entitledQty.multiply(ratePerShare);
                // Here we would typically credit a Cash Account, but for now we log it as a
                // process
                System.out.println("ENTITLEMENT: Account " + pos.getAccountId() +
                        " entitled to " + cashAmount + " based on holding " +
                        entitledQty + " at " + event.getRecordDate());

                // Track this as an 'INCOME' transaction for audit
                positionService.updatePositionWithProcess(
                        pos.getAccountId(),
                        event.getIsin(),
                        BigDecimal.ZERO, // No share movement for cash div
                        "RECE",
                        "INCOME",
                        "DIV-" + eventId);
            }
        }

        event.setStatus("COMPLETED");
        corporateActionRepository.save(event);
    }

    public List<EntitlementResult> calculateEntitlements(String eventId, BigDecimal rate) {
        CorporateActionEvent event = corporateActionRepository.findAll().stream()
                .filter(e -> e.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<EntitlementResult> results = new ArrayList<>();
        List<Position> allActiveHolders = positionRepository.findByIsin(event.getIsin());
        for (Position pos : allActiveHolders) {
            BigDecimal eligibleQty = positionService.getPositionAtTime(
                    pos.getAccountId(),
                    event.getIsin(),
                    event.getRecordDate());

            if (eligibleQty.compareTo(BigDecimal.ZERO) > 0) {
                results.add(new EntitlementResult(
                        pos.getAccountId(),
                        event.getIsin(),
                        eligibleQty,
                        eligibleQty.multiply(rate)));
            }
        }
        return results;
    }
}
