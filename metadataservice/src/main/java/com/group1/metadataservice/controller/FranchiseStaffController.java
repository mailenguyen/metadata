package com.group1.metadataservice.controller;

import com.group1.metadataservice.model.dto.FranchiseWithStaffResponse;
import com.group1.metadataservice.model.dto.StaffWithFranchisesResponse;
import com.group1.metadataservice.model.entity.FranchiseStaff;
import com.group1.metadataservice.model.entity.franchise.Franchise;
import com.group1.metadataservice.service.FranchiseStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/franchise-staff")
@RequiredArgsConstructor
public class FranchiseStaffController {

    private final FranchiseStaffService staffService;

    @PostMapping
    public ResponseEntity<FranchiseStaff> create(@RequestBody FranchiseStaff request) {
        // Giả sử JSON gửi lên là: {"staffId": "...", "franchise": {"id": "..."}}
        UUID franchiseId = request.getFranchise().getId();
        return ResponseEntity.ok(staffService.saveStaff(franchiseId, request.getStaffId()));
    }

    @GetMapping("/franchise/{franchiseId}")
    public ResponseEntity<List<FranchiseStaff>> getByFranchise(@PathVariable UUID franchiseId) {
        return ResponseEntity.ok(staffService.getAllByFranchiseId(franchiseId));
    }

    @GetMapping("/staff/{staffId}/franchise")
    public ResponseEntity<StaffWithFranchisesResponse>
    getFranchiseByStaffId(@PathVariable String staffId) {

        return ResponseEntity.ok(
                staffService.getFranchiseByStaffId(staffId)
        );
    }
}
