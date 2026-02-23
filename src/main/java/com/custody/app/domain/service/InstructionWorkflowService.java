package com.custody.app.domain.service;

import com.custody.app.adapter.out.InstructionRepository;
import com.custody.app.domain.model.Instruction;
import com.custody.app.iso20022.model.Sese023Document;
import com.custody.app.iso20022.model.Sese023Document.QuantityAndAccountDetails;
import com.custody.app.iso20022.model.Sese023Document.SecuritiesSettlementTransactionInstruction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class InstructionWorkflowService {

    private final InstructionRepository instructionRepository;
    private final MatchingService matchingService;
    private final PositionService positionService;

    public InstructionWorkflowService(InstructionRepository instructionRepository,
            MatchingService matchingService,
            PositionService positionService) {
        this.instructionRepository = instructionRepository;
        this.matchingService = matchingService;
        this.positionService = positionService;
    }

    @Transactional
    public String processDepositoryInstruction(Sese023Document document) {
        SecuritiesSettlementTransactionInstruction instr = document.getInstruction();
        String txId = instr.getTransactionId();
        if (txId == null || txId.isEmpty())
            txId = "SETL-" + System.currentTimeMillis();

        String isin = instr.getFinancialInstrumentId() != null ? instr.getFinancialInstrumentId().getIsin() : null;
        QuantityAndAccountDetails qad = instr.getQuantityAndAccountDetails();
        String accountId = qad != null && qad.getSafekeepingAccount() != null ? qad.getSafekeepingAccount().getId()
                : null;
        BigDecimal qty = (qad != null && qad.getSettlementQuantity() != null
                && qad.getSettlementQuantity().getQuantity() != null)
                        ? qad.getSettlementQuantity().getQuantity().getUnit()
                        : null;
        String movementType = instr.getSettlementType() != null ? instr.getSettlementType().getSecuritiesMovementType()
                : null;

        // Step 2: Validate fields
        if (isin == null || accountId == null || qty == null || movementType == null) {
            rejectInstruction(txId, isin, accountId, qty, movementType);
            return "Instruction REJECTED due to missing fields: " + txId;
        }

        // Step 1: Capture Instruction
        Instruction instruction = new Instruction(txId, isin, accountId, qty, movementType, "DEPOSITORY", "VALIDATED");
        instructionRepository.save(instruction);

        // Step 3: Update Positions Immediately (as requested: "every time instructions
        // are processed")
        positionService.updatePositionWithProcess(accountId, isin, qty, movementType, "SETTLEMENT", txId);

        // Step 4: Attempt Matching
        Optional<Instruction> match = matchingService.findPerfectMatch(instruction);
        if (match.isPresent()) {
            matchingService.performMatch(instruction, match.get());

            instruction.setStatus("SETTLED");
            match.get().setStatus("SETTLED");
            instructionRepository.save(instruction);
            instructionRepository.save(match.get());

            return "Instruction MATCHED and SETTLED: " + txId;
        } else {
            // Even if not matched yet, we've updated positions as requested
            instruction.setStatus("SETTLED_UNMATCHED"); // Indicating it updated inventory but has no internal pair
            instructionRepository.save(instruction);
            return "Instruction SETTLED (Position Updated), Pending Match: " + txId;
        }
    }

    private void rejectInstruction(String txId, String isin, String accountId, BigDecimal qty, String movementType) {
        Instruction instruction = new Instruction(txId, isin, accountId, qty, movementType, "DEPOSITORY", "REJECTED");
        instructionRepository.save(instruction);
        System.out.println("Instruction REJECTED: " + txId);
    }
}
