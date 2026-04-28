package com.example.monolith.reservation.controller;

import com.example.monolith.reservation.controller.dto.CreateReservationRequest;
import com.example.monolith.reservation.controller.dto.ReservationResponse;
import com.example.monolith.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@RequestBody @Valid CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping("/{id}")
    public ReservationResponse get(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        reservationService.deleteAll();
    }
}
