package com.group1.metadataservice.service.franchise;

import com.group1.metadataservice.model.dto.franchise.request.CreateFranchiseRequest;
import com.group1.metadataservice.model.dto.franchise.request.UpdateFranchiseRequest;
import com.group1.metadataservice.model.dto.franchise.response.FranchiseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FranchiseService {

    FranchiseResponse create(CreateFranchiseRequest request);

    List<FranchiseResponse> getAll();

    FranchiseResponse getById(UUID id);

    FranchiseResponse updateIdentity(UUID id, UpdateFranchiseRequest request, String changedBy);

    void activate(UUID id, String activatedBy);

    void deactivate(UUID id, String activatedBy);

    Page<FranchiseResponse> getAllByManager(UUID managerId, Pageable pageable);

}
