package com.optimax.pms.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms")
public class RoomAdminController {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomAdminController(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @GetMapping
    public List<Room> list(@RequestParam("propertyId") Long propertyId) {
        return roomRepository.findByPropertyId(propertyId);
    }

    @PostMapping
    public Room create(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @GetMapping("/types")
    public List<RoomType> listTypes(@RequestParam("propertyId") Long propertyId) {
        return roomTypeRepository.findByPropertyId(propertyId);
    }

    @PostMapping("/types")
    public RoomType createType(@RequestBody RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }
}

