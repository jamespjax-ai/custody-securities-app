package com.custody.app.adapter.in;

import com.custody.app.domain.service.OutboundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/outbound")
public class OutboundController {

    private final OutboundService outboundService;

    public OutboundController(OutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateInstruction(
            @RequestParam String isin,
            @RequestParam String accountId,
            @RequestParam BigDecimal quantity,
            @RequestParam String movementType) {
        
        String txId = UUID.randomUUID().toString();
        String xml = outboundService.generateSettlementInstruction(txId, isin, accountId, quantity, movementType);
        
        if (xml.startsWith("Error")) {
            return ResponseEntity.badRequest().body(xml);
        }
        return ResponseEntity.ok(xml);
    }
}
