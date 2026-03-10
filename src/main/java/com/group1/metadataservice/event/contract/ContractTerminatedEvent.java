package com.group1.metadataservice.event.contract;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContractTerminatedEvent(
        UUID contractId,
        UUID franchiseId,
        String reason,
        String terminatedBy,
        LocalDateTime timestamp
) {}
