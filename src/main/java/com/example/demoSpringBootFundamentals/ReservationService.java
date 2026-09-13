package com.example.demoSpringBootFundamentals;

import java.time.LocalDate;
import java.util.List;

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
    //метод который отдаст список брнирований
    public List<Reservation> findAllReservation() {
        return List.of(
            new Reservation(
                1L,
                100L,
                40L,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                ReservationStatus.APPROVED
                ),
            new Reservation(
                2L,
                100L,
                40L,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                ReservationStatus.APPROVED
                )
        );
    }
}
