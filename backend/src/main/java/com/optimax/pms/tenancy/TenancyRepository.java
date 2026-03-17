package com.optimax.pms.tenancy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenancyRepository extends JpaRepository<TenancyNode, Long> {

    List<TenancyNode> findByParentId(Long parentId);

    List<TenancyNode> findByLevel(TenancyLevel level);
}

