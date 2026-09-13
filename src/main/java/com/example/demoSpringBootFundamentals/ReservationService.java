package com.example.demoSpringBootFundamentals;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service 
public class ReservationService {
    public Reservation getReservationById(Long id) {
        return new Reservation(
            id,
            100L,
            40L,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            ReservationStatus.APPROVED
        );
    }
}
