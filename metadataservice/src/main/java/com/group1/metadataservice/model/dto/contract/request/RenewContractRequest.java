package com.group1.metadataservice.model.dto.contract.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RenewContractRequest(
        @NotNull(message = "New end date must not be null")
        LocalDate newEndDate
) {}
