package com.example.monolith.reservation.service;

import com.example.monolith.payment.controller.dto.CreatePaymentRequest;
import com.example.monolith.payment.service.PaymentService;
import com.example.monolith.reservation.controller.dto.CreateReservationRequest;
import com.example.monolith.reservation.controller.dto.ReservationResponse;
import com.example.monolith.reservation.entity.Reservation;
import com.example.monolith.reservation.entity.ReservationStatus;
import com.example.monolith.reservation.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PaymentService paymentService;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        if (!request.from().isBefore(request.to())) {
            throw new IllegalArgumentException("from must be before to");
        }

        Reservation reservation = Reservation.builder()
                .userId(request.userId())
                .resourceId(request.resourceId())
                .startTime(request.from())
                .endTime(request.to())
                .status(ReservationStatus.CREATED)
                .build();

        reservation = reservationRepository.save(reservation);

        paymentService.create(
                new CreatePaymentRequest(
                        reservation.getId(),
                        new BigDecimal(100)
                )
        );

        return toResponse(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found: " + id));
    }

    @Transactional
    public void deleteAll() {
        reservationRepository.deleteAll();
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getResourceId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus()
        );
    }
}
