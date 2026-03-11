package com.group1.metadataservice.model.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.group1.metadataservice.model.entity.franchise.Franchise;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FranchiseWithStaffResponse {
    private Franchise franchise;
    private JsonNode staff;
}