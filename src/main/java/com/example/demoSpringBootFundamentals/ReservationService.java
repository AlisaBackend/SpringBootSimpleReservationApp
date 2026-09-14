package com.example.demoSpringBootFundamentals;

// import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

//import javax.naming.spi.ResolveResult;

//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestBody;

//бзнес логикой занимаеся сервис
@Service 
public class ReservationService {

    //Map для хранения Reservation по id
    private final Map<Long, Reservation> reservationMap;
    //чтобы избавиться от паралельных запросов для разных потоков
    private final AtomicLong idCounter; //счетчик для id
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
        reservationMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    //метод для проверки 
    public Reservation getReservationById(Long id){
        if (reservationMap.containsKey(id)){
            throw new NoSuchElementException("Not found resesrvation by id = " + id);
        }
        return reservationMap.get(id);
    }


    //метод который отдаст список бронирований
    //рапиши подробно что происходит с 66 строчкой
    public List<Reservation> findAllReservation() {
        List<ReservationEntity> allEntities = repository.findAll();
        List<Reservation> resertionList = allEntities.stream()
            .map(it -> 
                new Reservation( //сущность
                    it.getId(),
                    it.getUserId(),
                    it.getRoomId(),
                    it.getStartDate(),
                    it.getEndDate(),
                    it.getStatus()
                )
            ).toList();
        return resertionList;
        //return reservationMap.values().stream().toList();
    }

    //реализуем логику в ReservationService для createReservation()
    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.id() != null) {
            throw new IllegalArgumentException("Id should be empty");
        }
        //проверка чтобы пользователь не задал сам себе статус
        if (reservationToCreate.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }
        var newReservation = new Reservation(
            idCounter.incrementAndGet(),
            reservationToCreate.userId(),
            reservationToCreate.roomId(),
            reservationToCreate.startDate(),
            reservationToCreate.endDate(),
            ReservationStatus.PENDING
        );
        reservationMap.put(newReservation.id(), newReservation);
        return newReservation;
    }

    public Reservation updateReservation(
        Long id,
        Reservation reservationToUpdate
    ) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found resesrvation by id = " + id);
        }
        var reservation = reservationMap.get(id);
        if (reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify reservation: status=" + reservation);
        }
        var updatedReservation = new Reservation(
            reservation.id(),
            reservationToUpdate.userId(),
            reservationToUpdate.roomId(),
            reservationToUpdate.startDate(),
            reservationToUpdate.endDate(),
            ReservationStatus.PENDING
        );
        reservationMap.put(reservation.id(), updatedReservation);
        return updatedReservation;
    }

    public void deleteReservation(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found resesrvation by id = " + id);
        }
        reservationMap.remove(id);
    }

    public Reservation approveReservation(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found resesrvation by id = " + id);
        }
        var reservation = reservationMap.get(id);
        if(reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot approve reservation: status=" + reservation.status());
        }
        var isConflict = isReservationConflict(reservation);
        if (isConflict) {
            throw new IllegalStateException("Cannot approve reservation because of conflict");
        }
        var approvedReservation = new Reservation(
            reservation.id(),
            reservation.userId(),
            reservation.roomId(),
            reservation.startDate(),
            reservation.endDate(),
            ReservationStatus.APPROVED
        );
        reservationMap.put(reservation.id(), approvedReservation);
        return approvedReservation;
    }

    //метод для проверк нет ли пересечений
    private boolean isReservationConflict(Reservation reservation) {
        for (Reservation existingReservation: reservationMap.values()) {
            if (reservation.id().equals(existingReservation.id())) {
                continue;
            }
            if (!reservation.roomId().equals(existingReservation.roomId())) {
                continue;
            }
            if (!existingReservation.status().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (reservation.startDate().isBefore(existingReservation.endDate())
                && existingReservation.startDate().isBefore(reservation.endDate())) {
                return true;
            }
        }
        return false;
    }
}