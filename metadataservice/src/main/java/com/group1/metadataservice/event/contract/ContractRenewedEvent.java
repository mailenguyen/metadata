package com.group1.metadataservice.event.contract;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContractRenewedEvent(
        UUID contractId,
        UUID franchiseId,
        LocalDate newEndDate,
        String renewedBy,
        LocalDateTime timestamp
) {}
