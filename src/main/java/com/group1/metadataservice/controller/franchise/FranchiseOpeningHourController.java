package com.group1.metadataservice.controller.franchise;

import com.group1.metadataservice.common.response.ApiResponse;
import com.group1.metadataservice.model.dto.franchise.request.UpdateOpeningHoursRequest;
import com.group1.metadataservice.model.dto.franchise.response.OpeningHourResponse;
import com.group1.metadataservice.service.franchise.FranchiseOpeningHourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/franchises-service")
@RequiredArgsConstructor
public class FranchiseOpeningHourController {

    private final FranchiseOpeningHourService service;

    @PutMapping("/{franchiseId}/opening-hours")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<OpeningHourResponse> updateOpeningHours(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody UpdateOpeningHoursRequest request
    ) {
        OpeningHourResponse resp = service.updateOpeningHours(franchiseId, request);
        return ApiResponse.success(resp);
    }
}
