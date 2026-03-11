package com.group1.metadataservice.repository;

import com.group1.metadataservice.model.entity.FranchiseStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FranchiseStaffRepository extends JpaRepository<FranchiseStaff, UUID> {

    // Cách 1: Sử dụng Query Method (Tự động sinh query dựa trên tên method)
    // Query sẽ truy vấn vào field 'franchise' và lấy field 'id' bên trong đó [cite: 1, 2]
    @Query("SELECT fs FROM FranchiseStaff fs WHERE fs.franchise.id = :franchiseId")
    List<FranchiseStaff> findAllByFranchiseId(UUID franchiseId);

    List<FranchiseStaff> findAllByStaffId(String staffId);

    // Cách 2: Sử dụng @Query với JPQL (Nếu bạn muốn viết tường minh)
    @Query("SELECT fs FROM FranchiseStaff fs WHERE fs.franchise.id = :franchiseId")
    Optional<FranchiseStaff> getStaffByFranchiseId(@Param("franchiseId") UUID franchiseId);
}