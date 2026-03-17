package com.group1.app.shift.controller;

import com.group1.app.common.response.ApiResponse;
import com.group1.app.shift.dto.request.StaffCreateRequest;
import com.group1.app.shift.dto.request.StaffStatusRequest;
import com.group1.app.shift.dto.response.StaffResponse;
import com.group1.app.shift.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shift-service/staffs")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @PostMapping
    public ApiResponse<StaffResponse> createStaff(@RequestBody @Valid StaffCreateRequest request) {
        return ApiResponse.<StaffResponse>builder().success(true).data(staffService.createStaff(request)).build();
    }

    @GetMapping
    public ApiResponse<Page<StaffResponse>> getAllStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<StaffResponse>>builder().success(true).data(staffService.getAllStaffs(page, size)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StaffResponse> getStaffById(@PathVariable String id) {
        return ApiResponse.<StaffResponse>builder().success(true).data(staffService.getStaffById(id)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<StaffResponse> updateStaffById(@PathVariable String id, @RequestBody @Valid StaffCreateRequest request) {
        return ApiResponse.<StaffResponse>builder().success(true).data(staffService.updateStaff(id, request)).build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<StaffResponse> updateStatus(
            @PathVariable String id,
            @RequestBody @Valid StaffStatusRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .message("Staff status updated successfully")
                .data(staffService.updateStatus(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStaffById(@PathVariable String id) {
        staffService.deleteStaff(id);
        return ApiResponse.<Void>builder().success(true).message("Staff deleted").build();
    }
}