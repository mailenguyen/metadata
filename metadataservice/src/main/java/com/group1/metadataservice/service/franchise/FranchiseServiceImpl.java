package com.group1.metadataservice.service.franchise;

import com.group1.metadataservice.common.exception.ApiException;
import com.group1.metadataservice.common.exception.ErrorCode;
import com.group1.metadataservice.event.franchise.FranchiseActivatedEvent;
import com.group1.metadataservice.event.franchise.FranchiseCreatedEvent;
import com.group1.metadataservice.mapper.franchise.FranchiseMapper;
import com.group1.metadataservice.model.dto.franchise.request.CreateFranchiseRequest;
import com.group1.metadataservice.model.dto.franchise.request.UpdateFranchiseRequest;
import com.group1.metadataservice.model.dto.franchise.response.FranchiseResponse;
import com.group1.metadataservice.model.entity.contract.ContractStatus;
import com.group1.metadataservice.model.entity.franchise.Franchise;
import com.group1.metadataservice.model.entity.franchise.FranchiseAudit;
import com.group1.metadataservice.model.entity.franchise.FranchiseStatus;
import com.group1.metadataservice.model.entity.franchise.OperationalConfig;
import com.group1.metadataservice.repository.contract.ContractRepository;
import com.group1.metadataservice.repository.franchise.FranchiseAuditRepository;
import com.group1.metadataservice.repository.franchise.FranchiseRepository;
import com.group1.metadataservice.repository.franchise.OperationalConfigRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class FranchiseServiceImpl implements FranchiseService {

    FranchiseRepository franchiseRepository;
    OperationalConfigRepository configRepository;
    FranchiseAuditRepository auditRepository;
    ContractRepository contractRepository;
    FranchiseMapper franchiseMapper;
    ApplicationEventPublisher eventPublisher;

    @Override
    public FranchiseResponse create(CreateFranchiseRequest request) {

        if (franchiseRepository.existsByFranchiseCode(request.franchiseCode())) {
            throw new ApiException(ErrorCode.FRANCHISE_ALREADY_EXISTS);
        }

        if (!ZoneId.getAvailableZoneIds().contains(request.timezone())) {
            throw new ApiException(ErrorCode.INVALID_TIMEZONE);
        }

        Franchise franchise = franchiseMapper.toEntity(request);
        franchise = franchiseRepository.save(franchise);

        OperationalConfig cfg = OperationalConfig.builder()
                .franchiseId(franchise.getId())
                .openingHoursConfigured(false)
                .menuProfileAssigned(false)
                .warehouseMappingConfigured(false)
                .build();
        configRepository.save(cfg);

        auditRepository.save(
                FranchiseAudit.builder()
                        .franchiseId(franchise.getId())
                        .fieldChanged("CREATION")
                        .oldValue(null)
                        .newValue("Franchise created: " + franchise.getFranchiseCode())
                        .changedBy(franchise.getCreatedBy())
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        eventPublisher.publishEvent(new FranchiseCreatedEvent(franchise.getId(), franchise.getFranchiseCode()));

        return franchiseMapper.toResponse(franchise);
    }

    @Override
    @Transactional(readOnly = true)
    public FranchiseResponse getById(UUID id) {
        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.FR_404_FRANCHISE_NOT_FOUND));
        return franchiseMapper.toResponse(franchise);
    }

    @Override
    public FranchiseResponse updateIdentity(UUID id, UpdateFranchiseRequest request, String changedBy) {
        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.FR_404_FRANCHISE_NOT_FOUND));

        if (!ZoneId.getAvailableZoneIds().contains(request.timezone())) {
            throw new ApiException(ErrorCode.INVALID_TIMEZONE);
        }

        String old = franchise.toString();

        franchise.setFranchiseName(request.franchiseName());
        franchise.setAddress(request.address());
        franchise.setRegion(request.region());
        franchise.setContactInfo(request.contactInfo());
        franchise.setTimezone(request.timezone());

        franchise = franchiseRepository.save(franchise);

        auditRepository.save(
                FranchiseAudit.builder()
                        .franchiseId(franchise.getId())
                        .fieldChanged("IDENTITY_UPDATE")
                        .oldValue(old)
                        .newValue(franchise.toString())
                        .changedBy(changedBy)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        return franchiseMapper.toResponse(franchise);
    }

    @Override
    public void activate(UUID id, String activatedBy) {
        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.FR_404_FRANCHISE_NOT_FOUND));

        if (!(franchise.getStatus() == FranchiseStatus.PENDING || franchise.getStatus() == FranchiseStatus.SUSPENDED)) {
            throw new ApiException(ErrorCode.INVALID_FRANCHISE_STATUS);
        }

        boolean hasActiveContract = contractRepository.existsByFranchiseIdAndStatus(franchise.getId(), ContractStatus.ACTIVE);
        if (!hasActiveContract) {
            throw new ApiException(ErrorCode.CANNOT_ACTIVATE_NO_ACTIVE_CONTRACT);
        }

        Optional<OperationalConfig> cfgOpt = configRepository.findByFranchiseId(franchise.getId());
        if (cfgOpt.isEmpty()) {
            throw new ApiException(ErrorCode.OPERATIONAL_CONFIG_INCOMPLETE);
        }
        OperationalConfig cfg = cfgOpt.get();
        if (!(cfg.isOpeningHoursConfigured() && cfg.isMenuProfileAssigned() && cfg.isWarehouseMappingConfigured())) {
            throw new ApiException(ErrorCode.OPERATIONAL_CONFIG_INCOMPLETE);
        }

        FranchiseStatus oldStatus = franchise.getStatus();
        franchise.setStatus(FranchiseStatus.LIVE);
        franchiseRepository.save(franchise);

        auditRepository.save(
                FranchiseAudit.builder()
                        .franchiseId(franchise.getId())
                        .fieldChanged("STATUS_CHANGE")
                        .oldValue(oldStatus.name())
                        .newValue(FranchiseStatus.LIVE.name())
                        .changedBy(activatedBy)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        eventPublisher.publishEvent(new FranchiseActivatedEvent(franchise.getId(), activatedBy, LocalDateTime.now()));
    }

    @Override
    public void deactivate(UUID id, String deactivatedBy) {

        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.FR_404_FRANCHISE_NOT_FOUND));

        if (franchise.getStatus() != FranchiseStatus.LIVE) {
            throw new ApiException(ErrorCode.INVALID_FRANCHISE_STATUS);
        }

        FranchiseStatus oldStatus = franchise.getStatus();
        franchise.setStatus(FranchiseStatus.SUSPENDED);

        franchiseRepository.save(franchise);

        auditRepository.save(
                FranchiseAudit.builder()
                        .franchiseId(franchise.getId())
                        .fieldChanged("STATUS_CHANGE")
                        .oldValue(oldStatus.name())
                        .newValue(FranchiseStatus.SUSPENDED.name())
                        .changedBy(deactivatedBy)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        eventPublisher.publishEvent(
                new FranchiseActivatedEvent(
                        franchise.getId(),
                        deactivatedBy,
                        LocalDateTime.now()
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FranchiseResponse> getAllByManager(UUID managerId, Pageable pageable) {

        Page<Franchise> franchises = franchiseRepository
                .findByOwnerId(managerId, pageable);

        return franchises.map(franchiseMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FranchiseResponse> getAll() {

        List<Franchise> franchises = franchiseRepository
                .findAll();

        return franchises.stream()
                .map(franchiseMapper::toResponse)
                .toList();
    }
}
