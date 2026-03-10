package com.group1.metadataservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CONFIG_CONFLICT(HttpStatus.CONFLICT, "CONFIG_002", "Config was modified by another user"),
    CONFIG_NOT_FOUND(HttpStatus.NOT_FOUND, "CONFIG_001", "Configuration not found"),
    INVALID_REGION(HttpStatus.BAD_REQUEST, "REGION_001", "Invalid region"),
    INVALID_KEY(HttpStatus.BAD_REQUEST, "KEY_001", "Invalid metadata key format"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_999", "Internal server error"),

    // ===== CONTRACT =====
    CT_001_CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "CT_001", "Contract not found"),
    CT_002_ACTIVE_CONTRACT_EXISTS(HttpStatus.BAD_REQUEST, "CT_002", "Active contract already exists"),
    CT_003_INVALID_CONTRACT_DATE(HttpStatus.BAD_REQUEST, "CT_003", "Invalid contract date range"),
    CT_004_DUPLICATE_CONTRACT_NUMBER(HttpStatus.BAD_REQUEST, "CT_004", "Duplicate contract number"),
    CT_005_INVALID_ROYALTY_RATE(HttpStatus.BAD_REQUEST, "CT_005", "Invalid royalty rate"),
    CT_006_INVALID_CONTRACT_STATUS(HttpStatus.BAD_REQUEST, "CT_006", "Invalid contract status"),
    CT_007_CONTRACT_DATE_OVERLAP(HttpStatus.BAD_REQUEST, "CT_007", "Contract date overlaps with existing contract"),

    // ===== FRANCHISE =====
    FR_404_FRANCHISE_NOT_FOUND(HttpStatus.NOT_FOUND, "FR_404", "Franchise not found"),

    FRANCHISE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "FR_006", "Franchise code already exists"),
    INVALID_TIMEZONE(HttpStatus.BAD_REQUEST, "FR_007", "Timezone is not supported"),
    OPERATIONAL_CONFIG_INCOMPLETE(HttpStatus.BAD_REQUEST, "FR_008", "Operational configuration incomplete"),
    CANNOT_ACTIVATE_NO_ACTIVE_CONTRACT(HttpStatus.BAD_REQUEST, "FR_009", "Cannot activate franchise without active contract"),
    INVALID_FRANCHISE_STATUS(HttpStatus.CONFLICT, "FR_010", "Invalid franchise status for this operation"),

    // ===== OPENING HOURS =====
    OH_001_BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "OH_001", "Brand not found for franchise"),
    OH_002_INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "OH_002", "Close time must be after open time"),
    OH_003_EXCEEDS_MAX_HOURS(HttpStatus.BAD_REQUEST, "OH_003", "Opening hours exceed brand maximum hours per day");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
