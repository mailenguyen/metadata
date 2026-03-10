package com.group1.metadataservice.model.entity.franchise;

import com.group1.metadataservice.infrastructure.persistence.base.BaseEntity;
import com.group1.metadataservice.model.entity.FranchiseStaff;
import com.group1.metadataservice.model.entity.brand.Brand;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "franchises", uniqueConstraints = @UniqueConstraint(columnNames = "franchiseCode"))
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Franchise extends BaseEntity {

    @Column(nullable = false)
    private String franchiseName;

    @Column(nullable = false, unique = true)
    private String franchiseCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String timezone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FranchiseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingStatus onboardingStatus;

    @Column(nullable = false)
    private boolean featureFlags;

    private UUID ownerId;

    private String contactInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "franchise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FranchiseStaff> franchiseStaffs;

}

