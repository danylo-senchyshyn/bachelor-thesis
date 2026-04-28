package com.example.monolith.payment.service;

import com.example.monolith.notification.service.NotificationService;
import com.example.monolith.payment.controller.dto.CreatePaymentRequest;
import com.example.monolith.payment.controller.dto.PaymentConfirmedRequest;
import com.example.monolith.payment.controller.dto.PaymentFailedRequest;
import com.example.monolith.payment.controller.dto.PaymentResponse;
import com.example.monolith.payment.entity.Payment;
import com.example.monolith.payment.entity.PaymentStatus;
import com.example.monolith.payment.repository.PaymentRepository;import com.example.monolith.reservation.entity.ReservationStatus;
import com.example.monolith.reservation.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final ReservationRepository reservationRepository;

    @Transactional
    public PaymentResponse create(CreatePaymentRequest request) {
        if (paymentRepository.existsByReservationId(request.reservationId())) {
            throw new IllegalStateException("Payment already exists");
        }

        Payment payment = Payment.builder()
                .reservationId(request.reservationId())
                .amount(request.amount())
                .status(PaymentStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse confirm(Long id) {
        Payment payment = getPayment(id);

        if (payment.getStatus() != PaymentStatus.CREATED) {
            return toResponse(payment);
        }

        payment.setStatus(PaymentStatus.CONFIRMED);

        reservationRepository.findById(payment.getReservationId()).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.PAID);
        });

        notificationService.paymentConfirmed(payment.getId());

        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse fail(Long id) {
        Payment payment = getPayment(id);

        if (payment.getStatus() != PaymentStatus.CREATED) {
            return toResponse(payment);
        }

        payment.setStatus(PaymentStatus.FAILED);

        reservationRepository.findById(payment.getReservationId()).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
        });

        notificationService.paymentFailed(payment.getId());

        return toResponse(payment);
    }

    public List<PaymentResponse> getByReservationId(Long reservationId) {
        return paymentRepository.findAllByReservationId(reservationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteAll() {
        paymentRepository.deleteAll();
    }

    private Payment getPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> {
                    return new EntityNotFoundException(
                            "Payment with id " + id + " not found"
                    );
                });
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getReservationId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
