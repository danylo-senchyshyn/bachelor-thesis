package com.example.monolith.payment.controller;

import com.example.monolith.payment.controller.dto.CreatePaymentRequest;
import com.example.monolith.payment.controller.dto.FailPaymentRequest;
import com.example.monolith.payment.controller.dto.PaymentResponse;
import com.example.monolith.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentResponse> create(@RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(paymentService.create(request));
    }

    @GetMapping("/by-reservation/{reservationId}")
    public ResponseEntity<List<PaymentResponse>> getByReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.getByReservationId(reservationId));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentResponse> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.confirm(id));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<PaymentResponse> confirmPatch(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.confirm(id));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentResponse> fail(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.fail(id));
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<PaymentResponse> failPatch(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.fail(id));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        paymentService.deleteAll();
    }
}
