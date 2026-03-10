package com.group1.metadataservice.model.entity.contract;

import com.group1.metadataservice.infrastructure.persistence.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contracts",
        uniqueConstraints = @UniqueConstraint(columnNames = "contractNumber"),
        indexes = {
                @Index(name = "idx_contract_franchise_status", columnList = "franchiseId,status"),
                @Index(name = "idx_contract_overlap", columnList = "franchiseId,status,startDate,endDate")
        })
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Contract extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String contractNumber;

    @Column(nullable = false)
    private UUID franchiseId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal royaltyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @Column(nullable = false)
    private boolean autoOrderEnabled;

    private LocalDateTime activatedAt;
    private String activatedBy;
}
