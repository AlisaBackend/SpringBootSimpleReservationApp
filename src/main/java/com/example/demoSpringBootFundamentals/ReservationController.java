package com.example.demoSpringBootFundamentals;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //http://localhost:8080/11  -например
    @GetMapping("/{id}")
    public Reservation getReservationById(
       @PathVariable("id") Long id
    ) {
        System.out.println("lod called method");
        return reservationService.getReservationById(id);
    }

    //Метод для выведения всех бронирований
    //http://localhost:8080/
    @GetMapping()
    public List<Reservation> getAllReservations() {
        System.out.println("log called getAllReservations method");
        return reservationService.findAllReservation();
    }

    
}
