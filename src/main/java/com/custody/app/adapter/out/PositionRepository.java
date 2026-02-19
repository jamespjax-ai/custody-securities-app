package com.custody.app.adapter.out;

import com.custody.app.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    java.util.Optional<Position> findByAccountIdAndIsin(String accountId, String isin);

    java.util.List<Position> findByIsin(String isin);
}
