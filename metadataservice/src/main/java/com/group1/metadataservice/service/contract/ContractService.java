package com.group1.metadataservice.service.contract;

import com.group1.metadataservice.model.dto.contract.request.CreateContractRequest;
import com.group1.metadataservice.model.dto.contract.request.RenewContractRequest;
import com.group1.metadataservice.model.dto.contract.request.TerminateContractRequest;
import com.group1.metadataservice.model.dto.contract.response.ActivateContractResponse;
import com.group1.metadataservice.model.dto.contract.response.CreateContractResponse;
import com.group1.metadataservice.model.dto.contract.response.RenewContractResponse;
import com.group1.metadataservice.model.dto.contract.response.TerminateContractResponse;
import com.group1.metadataservice.model.entity.contract.Contract;

import java.util.List;
import java.util.UUID;

public interface ContractService {

    CreateContractResponse create(CreateContractRequest request, String createdBy);

    RenewContractResponse renew(UUID id, RenewContractRequest request, String renewedBy);

    TerminateContractResponse terminate(
            UUID id,
            TerminateContractRequest request,
            String terminatedBy);

    ActivateContractResponse activate(UUID id, String activatedBy);

    Contract getById(UUID id);

    List<Contract> getAll();
}
