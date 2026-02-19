package com.custody.app.adapter.in;

import com.custody.app.domain.model.CorporateActionEvent;
import com.custody.app.domain.service.CorporateActionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/corporate-actions")
public class CorporateActionController {

    private final CorporateActionService corporateActionService;

    public CorporateActionController(CorporateActionService corporateActionService) {
        this.corporateActionService = corporateActionService;
    }

    @GetMapping
    public List<CorporateActionEvent> getAllEvents() {
        return corporateActionService.getAllEvents();
    }

    @PostMapping("/announce")
    public CorporateActionEvent announceEvent(@RequestBody CorporateActionEvent event) {
        return corporateActionService.announceEvent(event);
    }

    @PostMapping("/process/bonus")
    public void processBonus(@RequestParam String eventId, @RequestParam String isin, @RequestParam BigDecimal ratio) {
        corporateActionService.processBonusIssue(eventId, isin, ratio);
    }
}
