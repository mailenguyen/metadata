package com.group1.metadataservice.repository.franchise;

import com.group1.metadataservice.model.entity.franchise.Franchise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, UUID> {

    boolean existsByFranchiseCode(String franchiseCode);

    Page<Franchise> findByOwnerId(UUID ownerId, Pageable pageable);
}

