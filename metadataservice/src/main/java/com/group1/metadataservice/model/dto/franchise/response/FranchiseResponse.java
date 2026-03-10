package com.group1.metadataservice.model.dto.franchise.response;

import com.group1.metadataservice.model.entity.franchise.FranchiseStatus;
import com.group1.metadataservice.model.entity.franchise.OnboardingStatus;

import java.util.UUID;

public record FranchiseResponse(
        UUID franchiseId,
        String franchiseName,
        String franchiseCode,
        String address,
        String region,
        String timezone,
        FranchiseStatus status,
        OnboardingStatus onboardingStatus,
        boolean featureFlags,
        UUID ownerId,
        String contactInfo
) {}

