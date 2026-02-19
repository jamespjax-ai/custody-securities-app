package com.custody.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "securities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Security {

    @Id
    private String isin;
    private String name;
    private String country;
    private String assetClass; // EQUITY, BOND, etc.
}
