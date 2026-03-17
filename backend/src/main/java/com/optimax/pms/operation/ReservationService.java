package com.optimax.pms.operation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findForPropertyAndRange(Long propertyId, LocalDate from, LocalDate to) {
        return reservationRepository.findByPropertyIdAndCheckInBetween(propertyId, from, to);
    }

    @Transactional
    public Reservation create(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}

