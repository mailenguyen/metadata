package com.group1.metadataservice.service;

import com.group1.metadataservice.infrastructure.StaffClient;
import com.group1.metadataservice.model.FranchiseDto;
import com.group1.metadataservice.model.dto.FranchiseWithStaffResponse;
import com.group1.metadataservice.model.dto.StaffWithFranchisesResponse;
import com.group1.metadataservice.model.entity.FranchiseStaff;
import com.group1.metadataservice.model.entity.franchise.Franchise;
import com.group1.metadataservice.repository.FranchiseStaffRepository;
import com.group1.metadataservice.repository.franchise.FranchiseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class FranchiseStaffService {

    private final FranchiseStaffRepository franchiseStaffRepository;
    private final FranchiseRepository franchiseRepository;
    private final StaffClient staffClient;

    public FranchiseStaff saveStaff(UUID franchiseId, String staffId) {
        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new RuntimeException("Franchise not found"));

        FranchiseStaff staff = new FranchiseStaff();
        staff.setStaffId(staffId);
        staff.setFranchise(franchise);

        return franchiseStaffRepository.save(staff);
    }

    @Transactional(readOnly = true)
    public StaffWithFranchisesResponse getFranchiseByStaffId(String staffId) {

        List<FranchiseStaff> mappings =
                franchiseStaffRepository.findAllByStaffId(staffId);

        if (mappings.isEmpty()) {
            throw new RuntimeException("Mapping not found");
        }

        List<FranchiseDto> franchises = mappings.stream()
                .map(FranchiseStaff::getFranchise)
                .map(FranchiseDto::from)
                .toList();

        var staffDetail = staffClient.getStaffById(staffId);

        return new StaffWithFranchisesResponse(staffDetail, franchises);
    }

    @Transactional(readOnly = true)
    public List<FranchiseStaff> getAllByFranchiseId(UUID franchiseId) {
        return franchiseStaffRepository.findAllByFranchiseId(franchiseId);
    }
}