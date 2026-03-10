package com.group1.metadataservice.controller.franchise;

import com.group1.metadataservice.common.response.ApiResponse;
import com.group1.metadataservice.model.dto.franchise.request.CreateFranchiseRequest;
import com.group1.metadataservice.model.dto.franchise.request.UpdateFranchiseRequest;
import com.group1.metadataservice.model.dto.franchise.response.FranchiseResponse;
import com.group1.metadataservice.service.franchise.FranchiseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    private String getCurrentUser(Authentication authentication) {
        return authentication.getName();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FranchiseResponse> create(
            @Valid @RequestBody CreateFranchiseRequest body,
            Authentication authentication) {

        return ApiResponse.success(
                franchiseService.create(body)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ApiResponse<FranchiseResponse> getById(@PathVariable UUID id) {
        return ApiResponse.success(
                franchiseService.getById(id)
        );
    }

    @PutMapping("/{id}/identity")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FranchiseResponse> updateIdentity(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFranchiseRequest body,
            Authentication authentication) {

        return ApiResponse.success(
                franchiseService.updateIdentity(id, body, getCurrentUser(authentication))
        );
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> activate(
            @PathVariable UUID id,
            Authentication authentication) {

        franchiseService.activate(id, getCurrentUser(authentication));
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deactivate(
            @PathVariable UUID id,
            Authentication authentication) {

        franchiseService.deactivate(id, getCurrentUser(authentication));
        return ApiResponse.success(null);
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<Page<FranchiseResponse>> getMyFranchises(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        UUID managerId = UUID.fromString(authentication.getName());

        return ApiResponse.success(
                franchiseService.getAllByManager(managerId, pageable)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<FranchiseResponse>> getAll() {
        return ApiResponse.success(
                franchiseService.getAll()
        );
    }
}