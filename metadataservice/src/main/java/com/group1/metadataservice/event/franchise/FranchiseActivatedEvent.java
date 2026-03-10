package com.group1.metadataservice.event.franchise;

import java.time.LocalDateTime;
import java.util.UUID;

public record FranchiseActivatedEvent(
        UUID franchiseId,
        String activatedBy,
        LocalDateTime timestamp
) {}

