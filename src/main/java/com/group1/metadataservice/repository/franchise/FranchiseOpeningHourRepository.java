package com.group1.metadataservice.repository.franchise;

import com.group1.metadataservice.common.enums.DayOfWeekValue;
import com.group1.metadataservice.model.entity.franchise.FranchiseOpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FranchiseOpeningHourRepository extends JpaRepository<FranchiseOpeningHour, UUID> {

    Optional<FranchiseOpeningHour> findByFranchise_IdAndDayOfWeek(UUID franchiseId, DayOfWeekValue dayOfWeek);

}
