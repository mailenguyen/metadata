package com.group1.metadataservice.model;

import com.group1.metadataservice.model.entity.franchise.Franchise;

import java.util.UUID;

public record FranchiseDto(
        UUID franchiseId,
        String franchiseName,
        String franchiseCode,
        String address,
        String region,
        String timezone,
        String status
) {
    public static FranchiseDto from(Franchise f) {
        return new FranchiseDto(
                f.getId(),
                f.getFranchiseName(),
                f.getFranchiseCode(),
                f.getAddress(),
                f.getRegion(),
                f.getTimezone(),
                f.getStatus().name()
        );
    }
}
