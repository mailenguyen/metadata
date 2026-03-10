package com.group1.metadataservice.model.dto.contract.response;

import java.util.UUID;

public record TerminateContractResponse(
        UUID contractId,
        String status
) {}
