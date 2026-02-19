package com.custody.app.adapter.in;

import com.custody.app.domain.model.Position;
import com.custody.app.adapter.out.PositionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionRepository positionRepository;

    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @GetMapping
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @GetMapping("/{accountId}")
    public List<Position> getPositionsByAccount(@PathVariable String accountId) {
        // This requires a custom method in repo or filtering. 
        // For MVP, if we don't have findByAccountId, we can return all or filter in stream.
        // Or add findByAccountId to repo.
        return positionRepository.findAll().stream()
                .filter(p -> p.getAccountId().equals(accountId))
                .toList();
    }
}
