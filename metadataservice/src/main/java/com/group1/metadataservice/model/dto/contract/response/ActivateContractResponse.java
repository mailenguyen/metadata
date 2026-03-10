package com.group1.metadataservice.model.dto.contract.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivateContractResponse(
        UUID contractId,
        UUID franchiseId,
        String status,
        LocalDateTime activatedAt,
        String activatedBy
) {}
