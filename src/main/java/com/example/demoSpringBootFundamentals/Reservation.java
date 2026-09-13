package com.example.demoSpringBootFundamentals;

import java.time.LocalDate;

public record Reservation(
    Long id,
    Long userId,
    Long roomId,
    LocalDate startDate,
    LocalDate eddDate,
    ReservationStatus status
) {
    
}
