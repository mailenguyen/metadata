package com.group1.metadataservice.repository.contract;

import com.group1.metadataservice.model.entity.contract.Contract;
import com.group1.metadataservice.model.entity.contract.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {

    boolean existsByContractNumber(String contractNumber);

    @Query("""
        SELECT COUNT(c) > 0 FROM Contract c
        WHERE c.franchiseId = :franchiseId
        AND c.status = 'ACTIVE'
        AND (c.startDate <= :endDate AND c.endDate >= :startDate)
    """)
    boolean existsOverlappingActiveContract(UUID franchiseId, LocalDate startDate, LocalDate endDate);

    boolean existsByFranchiseIdAndStatus(UUID franchiseId, ContractStatus status);
}
