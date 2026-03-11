package com.group1.metadataservice.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group1.metadataservice.infrastructure.persistence.base.BaseEntity;
import com.group1.metadataservice.model.entity.brand.Brand;
import com.group1.metadataservice.model.entity.franchise.Franchise;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "franchisesStaff")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseStaff extends BaseEntity {

    @Column(nullable = false)
    private String staffId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchise;

}
