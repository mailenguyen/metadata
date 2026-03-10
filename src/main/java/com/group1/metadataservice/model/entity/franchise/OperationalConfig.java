package com.group1.metadataservice.model.entity.franchise;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "operational_configs")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OperationalConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID franchiseId;

    private boolean openingHoursConfigured;

    private boolean menuProfileAssigned;

    private boolean warehouseMappingConfigured;
}

