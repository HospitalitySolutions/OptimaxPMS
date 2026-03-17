package com.optimax.pms.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    List<RoomType> findByPropertyId(Long propertyId);
}

