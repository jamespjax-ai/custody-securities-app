package com.custody.app.adapter.out;

import com.custody.app.domain.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    Optional<Instruction> findByInstructionId(String instructionId);

    List<Instruction> findByStatus(String status);

    List<Instruction> findByAccountIdAndIsinAndStatus(String accountId, String isin, String status);
}
