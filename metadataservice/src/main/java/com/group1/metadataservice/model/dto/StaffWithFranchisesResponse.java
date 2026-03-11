package com.group1.metadataservice.model.dto;

import com.group1.metadataservice.model.FranchiseDto;
import com.group1.metadataservice.model.entity.franchise.Franchise;

import java.util.List;

public record StaffWithFranchisesResponse(
        Object staff,
        List<FranchiseDto> franchises
) {}
