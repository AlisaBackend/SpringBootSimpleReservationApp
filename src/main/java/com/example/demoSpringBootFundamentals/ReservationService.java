package com.example.demoSpringBootFundamentals;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service 
public class ReservationService {



    //Map для хранения Reservation по id
    private final Map<Long, Reservation> reservationMap = Map.of(
        1L, new Reservation(
            1L,
            100L,
            40L,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            ReservationStatus.APPROVED
        ),
        2L, new Reservation(
            2L,
            101L,
            41L,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            ReservationStatus.APPROVED
        ),
        3L, new Reservation(
            3L,
            102L,
            42L,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            ReservationStatus.APPROVED
        )
    );

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

    //метод для проверки 
    public Reservation getReservationById(Long id){
        if (reservationMap.containsKey(id)){
            throw new NoSuchElementException("Not found resesrvation by id = " + id);
        }
        return reservationMap.get(id);
    }


    //метод который отдаст список бронирований
    public List<Reservation> findAllReservation() {
        return reservationMap.values().stream().toList();
    }
}