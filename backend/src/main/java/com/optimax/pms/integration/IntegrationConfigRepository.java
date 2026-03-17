package com.optimax.pms.integration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntegrationConfigRepository extends JpaRepository<IntegrationConfig, Long> {

    List<IntegrationConfig> findByPropertyId(Long propertyId);
}

