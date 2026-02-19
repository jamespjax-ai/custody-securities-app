package com.custody.app.domain.service;

import com.custody.app.adapter.out.InstructionRepository;
import com.custody.app.domain.model.Instruction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchingService {

    private final InstructionRepository instructionRepository;

    public MatchingService(InstructionRepository instructionRepository) {
        this.instructionRepository = instructionRepository;
    }

    @Transactional
    public Optional<Instruction> findPerfectMatch(Instruction depositoryInstruction) {
        // Find an INTERNAL instruction with matching ISIN, Account, Quantity, and
        // opposite Movement Type
        List<Instruction> internalInstructions = instructionRepository.findByAccountIdAndIsinAndStatus(
                depositoryInstruction.getAccountId(),
                depositoryInstruction.getIsin(),
                "VALIDATED");

        return internalInstructions.stream()
                .filter(internal -> internal.getSource().equals("INTERNAL"))
                .filter(internal -> internal.getQuantity().compareTo(depositoryInstruction.getQuantity()) == 0)
                .filter(internal -> !internal.getMovementType().equals(depositoryInstruction.getMovementType()))
                .findFirst();
    }

    @Transactional
    public void performMatch(Instruction depositoryInstruction, Instruction internalInstruction) {
        String matchingId = UUID.randomUUID().toString();

        depositoryInstruction.setStatus("MATCHED");
        depositoryInstruction.setMatchingId(matchingId);

        internalInstruction.setStatus("MATCHED");
        internalInstruction.setMatchingId(matchingId);

        instructionRepository.save(depositoryInstruction);
        instructionRepository.save(internalInstruction);

        System.out.println("Perfect Match Found: " + matchingId + " for " + depositoryInstruction.getInstructionId()
                + " and " + internalInstruction.getInstructionId());
    }
}
