package com.group1.metadataservice.controller.contract;

import com.group1.metadataservice.common.response.ApiResponse;
import com.group1.metadataservice.model.dto.contract.request.CreateContractRequest;
import com.group1.metadataservice.model.dto.contract.request.RenewContractRequest;
import com.group1.metadataservice.model.dto.contract.request.TerminateContractRequest;
import com.group1.metadataservice.model.dto.contract.response.ActivateContractResponse;
import com.group1.metadataservice.model.dto.contract.response.CreateContractResponse;
import com.group1.metadataservice.model.dto.contract.response.RenewContractResponse;
import com.group1.metadataservice.model.dto.contract.response.TerminateContractResponse;
import com.group1.metadataservice.model.entity.contract.Contract;
import com.group1.metadataservice.service.contract.ContractService;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    private String getCurrentUser() {
        return "admin-test-user";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CreateContractResponse> create(
            @Valid @RequestBody CreateContractRequest body) {
        return ApiResponse.success(contractService.create(body, getCurrentUser()));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ActivateContractResponse> activate(@PathVariable UUID id) {
        return ApiResponse.success(contractService.activate(id, getCurrentUser()));
    }

    @PutMapping("/{id}/renew")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RenewContractResponse> renew(
            @PathVariable UUID id,
            @Valid @RequestBody RenewContractRequest body) {
        return ApiResponse.success(contractService.renew(id, body, getCurrentUser()));
    }

    @PutMapping("/{id}/terminate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TerminateContractResponse> terminate(
            @PathVariable UUID id,
            @Valid @RequestBody TerminateContractRequest body) {
        return ApiResponse.success(contractService.terminate(id, body, getCurrentUser()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<Contract> getById(@PathVariable UUID id) {
        return ApiResponse.success(contractService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<List<Contract>> getAll() {
        return ApiResponse.success(contractService.getAll());
    }
}
