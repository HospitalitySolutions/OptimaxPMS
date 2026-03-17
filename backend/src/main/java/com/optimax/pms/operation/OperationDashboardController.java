package com.optimax.pms.operation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/operation/dashboard")
public class OperationDashboardController {

    private final ReservationService reservationService;

    public OperationDashboardController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public Map<String, Object> summary(@RequestParam("propertyId") Long propertyId,
                                       @RequestParam("from") String from,
                                       @RequestParam("to") String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        var reservations = reservationService.findForPropertyAndRange(propertyId, fromDate, toDate);

        long totalReservations = reservations.size();

        return Map.of(
                "totalReservations", totalReservations
        );
    }
}

