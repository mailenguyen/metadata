package com.group1.metadataservice.event.franchise;

import java.util.UUID;

public record FranchiseCreatedEvent(
        UUID franchiseId,
        String franchiseCode
) {}

