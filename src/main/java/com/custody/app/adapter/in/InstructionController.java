package com.custody.app.adapter.in;

import com.custody.app.adapter.out.InstructionRepository;
import com.custody.app.domain.model.Instruction;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructions")
public class InstructionController {

    private final InstructionRepository instructionRepository;

    public InstructionController(InstructionRepository instructionRepository) {
        this.instructionRepository = instructionRepository;
    }

    @GetMapping
    public List<Instruction> getAllInstructions() {
        return instructionRepository.findAll();
    }

    @PostMapping("/internal")
    public Instruction createInternalInstruction(@RequestBody InstructionRequest request) {
        Instruction instruction = new Instruction(
                "INT-" + UUID.randomUUID().toString().substring(0, 8),
                request.getIsin(),
                request.getAccountId(),
                request.getQuantity(),
                request.getMovementType(),
                "INTERNAL",
                "VALIDATED");
        return instructionRepository.save(instruction);
    }

    public static class InstructionRequest {
        private String isin;
        private String accountId;
        private BigDecimal quantity;
        private String movementType;

        public String getIsin() {
            return isin;
        }

        public void setIsin(String isin) {
            this.isin = isin;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public String getMovementType() {
            return movementType;
        }

        public void setMovementType(String movementType) {
            this.movementType = movementType;
        }
    }
}
