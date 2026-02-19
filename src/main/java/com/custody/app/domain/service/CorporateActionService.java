package com.custody.app.domain.service;

import com.custody.app.adapter.out.CorporateActionRepository;
import com.custody.app.domain.model.CorporateActionEvent;
import com.custody.app.domain.model.Position;
import com.custody.app.adapter.out.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
}
