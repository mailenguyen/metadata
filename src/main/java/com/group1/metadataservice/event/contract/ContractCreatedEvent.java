package com.group1.metadataservice.event.contract;

import java.util.UUID;

public record ContractCreatedEvent(
        UUID contractId,
        String contractNumber
) {}
