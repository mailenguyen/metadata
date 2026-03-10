package com.group1.metadataservice.event.contract;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContractActivatedEvent(
        UUID contractId,
        UUID franchiseId,
        String activatedBy,
        LocalDateTime timestamp
) {}
