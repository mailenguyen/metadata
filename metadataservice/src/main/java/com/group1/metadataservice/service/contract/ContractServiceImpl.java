package com.group1.metadataservice.service.contract;

import com.group1.metadataservice.common.exception.ApiException;
import com.group1.metadataservice.common.exception.ErrorCode;
import com.group1.metadataservice.event.contract.ContractActivatedEvent;
import com.group1.metadataservice.event.contract.ContractRenewedEvent;
import com.group1.metadataservice.event.contract.ContractTerminatedEvent;
import com.group1.metadataservice.mapper.contract.ContractMapper;
import com.group1.metadataservice.model.dto.contract.request.CreateContractRequest;
import com.group1.metadataservice.model.dto.contract.request.RenewContractRequest;
import com.group1.metadataservice.model.dto.contract.request.TerminateContractRequest;
import com.group1.metadataservice.model.dto.contract.response.ActivateContractResponse;
import com.group1.metadataservice.model.dto.contract.response.CreateContractResponse;
import com.group1.metadataservice.model.dto.contract.response.RenewContractResponse;
import com.group1.metadataservice.model.dto.contract.response.TerminateContractResponse;
import com.group1.metadataservice.model.entity.contract.Contract;
import com.group1.metadataservice.model.entity.contract.ContractAudit;
import com.group1.metadataservice.model.entity.contract.ContractStatus;
import com.group1.metadataservice.repository.contract.ContractAuditRepository;
import com.group1.metadataservice.repository.contract.ContractRepository;
import com.group1.metadataservice.service.EffectiveConfigService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ContractServiceImpl implements ContractService {

    ContractRepository contractRepository;
    ContractAuditRepository auditRepository;
    ApplicationEventPublisher eventPublisher;
    ContractMapper contractMapper;
    EffectiveConfigService effectiveConfigService;

    @Override
    public CreateContractResponse create(CreateContractRequest request, String createdBy) {

        validateFranchiseExists(request.franchiseId());

        if (!request.startDate().isBefore(request.endDate())) {
            throw new ApiException(ErrorCode.CT_003_INVALID_CONTRACT_DATE);
        }

        BigDecimal maxRoyalty = getMaxRoyaltyRateFromMetadata();

        if (request.royaltyRate().compareTo(BigDecimal.ZERO) < 0
                || request.royaltyRate().compareTo(maxRoyalty) > 0) {
            throw new ApiException(ErrorCode.CT_005_INVALID_ROYALTY_RATE,
                    "Royalty rate must be between 0 and " + maxRoyalty);
        }

        if (contractRepository.existsByContractNumber(request.contractNumber())) {
            throw new ApiException(ErrorCode.CT_004_DUPLICATE_CONTRACT_NUMBER);
        }

        if (contractRepository.existsOverlappingActiveContract(
                request.franchiseId(), request.startDate(), request.endDate())) {
            throw new ApiException(ErrorCode.CT_007_CONTRACT_DATE_OVERLAP);
        }

        Contract contract = contractMapper.toEntity(request);
        contract.setCreatedBy(createdBy);

        contract = contractRepository.save(contract);
        return contractMapper.toCreateResponse(contract);
    }

    @Override
    public ActivateContractResponse activate(UUID id, String activatedBy) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.CT_001_CONTRACT_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new ApiException(ErrorCode.CT_006_INVALID_CONTRACT_STATUS);
        }

        validateFranchiseExists(contract.getFranchiseId());

        if (contractRepository.existsByFranchiseIdAndStatus(
                contract.getFranchiseId(), ContractStatus.ACTIVE)) {
            throw new ApiException(ErrorCode.CT_002_ACTIVE_CONTRACT_EXISTS);
        }

        contract.setStatus(ContractStatus.ACTIVE);
        contract.setActivatedAt(LocalDateTime.now());
        contract.setActivatedBy(activatedBy);

        saveAuditLog(contract.getId(), ContractStatus.DRAFT,
                ContractStatus.ACTIVE, "ACTIVATED", activatedBy);

        eventPublisher.publishEvent(new ContractActivatedEvent(
                contract.getId(),
                contract.getFranchiseId(),
                activatedBy,
                contract.getActivatedAt()
        ));

        return new ActivateContractResponse(
                contract.getId(),
                contract.getFranchiseId(),
                ContractStatus.ACTIVE.name(),
                contract.getActivatedAt(),
                activatedBy
        );
    }

    @Override
    public RenewContractResponse renew(UUID id, RenewContractRequest request, String renewedBy) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.CT_001_CONTRACT_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new ApiException(ErrorCode.CT_006_INVALID_CONTRACT_STATUS);
        }

        if (!request.newEndDate().isAfter(contract.getEndDate())) {
            throw new ApiException(ErrorCode.CT_003_INVALID_CONTRACT_DATE);
        }

        contract.setEndDate(request.newEndDate());

        saveAuditLog(contract.getId(), ContractStatus.ACTIVE,
                ContractStatus.ACTIVE, "RENEW", renewedBy);

        eventPublisher.publishEvent(new ContractRenewedEvent(
                contract.getId(),
                contract.getFranchiseId(),
                contract.getEndDate(),
                renewedBy,
                LocalDateTime.now()
        ));

        return contractMapper.toRenewResponse(contract);
    }

    @Override
    public TerminateContractResponse terminate(UUID id,
                                               TerminateContractRequest request,
                                               String terminatedBy) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.CT_001_CONTRACT_NOT_FOUND));

        ContractStatus oldStatus = contract.getStatus();

        contract.setStatus(ContractStatus.TERMINATED);
        contract.setAutoOrderEnabled(false);

        saveAuditLog(contract.getId(), oldStatus,
                ContractStatus.TERMINATED, request.terminationReason(), terminatedBy);

        eventPublisher.publishEvent(new ContractTerminatedEvent(
                contract.getId(),
                contract.getFranchiseId(),
                request.terminationReason(),
                terminatedBy,
                LocalDateTime.now()
        ));

        return contractMapper.toTerminateResponse(contract);
    }

    private BigDecimal getMaxRoyaltyRateFromMetadata() {
        try {
            var config = effectiveConfigService.getEffectiveConfig("contract.royalty.max", null);
            return new BigDecimal(config.getConfigValue());
        } catch (Exception e) {
            return new BigDecimal("100");
        }
    }

    private void saveAuditLog(UUID contractId,
                              ContractStatus oldStatus,
                              ContractStatus newStatus,
                              String reason,
                              String changedBy) {

        auditRepository.save(ContractAudit.builder()
                .contractId(contractId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .reason(reason)
                .changedBy(changedBy)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private void validateFranchiseExists(UUID franchiseId) {

        /*
        boolean exists = franchiseFeignClient.checkFranchiseExists(franchiseId);
        if (!exists) throw new ApiException(ErrorCode.FR_404_FRANCHISE_NOT_FOUND);
        */

        boolean isMockValid = true;
        if (!isMockValid) {
            throw new ApiException(ErrorCode.FR_404_FRANCHISE_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Contract getById(UUID id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.CT_001_CONTRACT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contract> getAll() {
        return contractRepository.findAll();
    }
}
