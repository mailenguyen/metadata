package com.group1.metadataservice.model.dto.contract.response;


import java.util.UUID;

public record CreateContractResponse(
        UUID contractId,
        String contractNumber,
        String status
) {}
