package com.group1.metadataservice.model.dto.contract.response;

import java.time.LocalDate;
import java.util.UUID;

public record RenewContractResponse(
        UUID contractId,
        LocalDate newEndDate,
        String status
) {}
