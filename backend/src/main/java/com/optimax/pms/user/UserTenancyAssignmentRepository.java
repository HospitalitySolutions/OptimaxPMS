package com.optimax.pms.user;

import com.optimax.pms.tenancy.TenancyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTenancyAssignmentRepository extends JpaRepository<UserTenancyAssignment, UserTenancyAssignment.Id> {

    List<UserTenancyAssignment> findByUserId(Long userId);

    List<UserTenancyAssignment> findByUserIdAndLevel(Long userId, TenancyLevel level);

    Optional<UserTenancyAssignment> findByIdUserIdAndIdTenancyId(Long userId, Long tenancyId);
}

