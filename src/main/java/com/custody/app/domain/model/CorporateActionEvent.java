package com.custody.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "corporate_actions")
@Data
@NoArgsConstructor
public class CorporateActionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private String isin;
    private String eventType; // DIVI, DVCA, BONY, etc.
    private LocalDateTime exDate;
    private LocalDateTime recordDate;
    private LocalDateTime paymentDate;
    private String status; // ANNOUNCED, COMPLETED
}
