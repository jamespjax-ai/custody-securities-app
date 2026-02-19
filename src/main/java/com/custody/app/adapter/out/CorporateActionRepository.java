package com.custody.app.adapter.out;

import com.custody.app.domain.model.CorporateActionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorporateActionRepository extends JpaRepository<CorporateActionEvent, Long> {
    List<CorporateActionEvent> findByIsin(String isin);
}
