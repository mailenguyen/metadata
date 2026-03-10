package com.group1.metadataservice.service;

import com.group1.metadataservice.model.entity.FranchiseStaff;
import com.group1.metadataservice.model.entity.franchise.Franchise;
import com.group1.metadataservice.repository.FranchiseStaffRepository;
import com.group1.metadataservice.repository.franchise.FranchiseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class FranchiseStaffService {

    FranchiseStaffRepository franchiseStaffRepository;
    FranchiseRepository franchiseRepository;

    public FranchiseStaff saveStaff(UUID franchiseId, String staffId) {
        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new RuntimeException("Franchise not found"));

        FranchiseStaff staff = new FranchiseStaff();
        staff.setStaffId(staffId);
        staff.setFranchise(franchise);

        // Đã sửa: Sử dụng đúng repository cho FranchiseStaff
        return franchiseStaffRepository.save(staff);
    }

    @Transactional(readOnly = true)
    public FranchiseStaff getByFranchiseId(UUID franchiseId) {
        return franchiseStaffRepository.findByFranchiseId(franchiseId)
                .orElseThrow(() -> new RuntimeException("Staff not found for this franchise"));
    }
}