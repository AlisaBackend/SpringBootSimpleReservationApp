package com.example.demoSpringBootFundamentals;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/reservation")
public class ReservationController {

    // для логирования
    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //http://localhost:8080/11  -например
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
       @PathVariable("id") Long id
    ) {
        log.info("Called method getReservationById: id " + id);
        try {
            return ResponseEntity.status(HttpStatus.OK)  //status(200) == status(HttpStatus.OK)
                .body(reservationService.getReservationById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
            .build();
        }
        
    }

    //Метод для выведения всех бронирований
    //http://localhost:8080/
    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("Called method  getAllReservations()");
        return ResponseEntity.ok(reservationService.findAllReservation());
    }

    @PostMapping 
    public ResponseEntity<Reservation> createReservation(
        @RequestBody Reservation reservationToCreate
    ) {
        log.info("Called createReservation");
        return ResponseEntity.status(HttpStatus.CREATED) //CREATED == 201
        //.header("test-header", "123")                    
        .body(reservationService.createReservation(reservationToCreate));
        //return reservationService.createReservation(reservationToCreate);
    }

    //для обновления
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
        @PathVariable("id") Long id,
        @RequestBody Reservation reservationToUpdate
    ) {
        log.info("Called updateReservation id={}, reservationToUpdate={}",
            id, reservationToUpdate);
            var update = reservationService.updateReservation(id, reservationToUpdate); //updateReservation() реализуется в class ReservationService
            return ResponseEntity.ok(update);
    }

    //для удаления
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
        @PathVariable("id") Long id
    ) {
        log.info("Called deleteReservation: id={}", id);
        try {
            reservationService.deleteReservation(id);
        return ResponseEntity.ok()
        .build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
            .build();
        }
    }

    //подтверждение бронирования
    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(
        @PathVariable("id") Long id
    ) {
        log.info("Called approveResrvation: id={}", id);
        var reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }
}
