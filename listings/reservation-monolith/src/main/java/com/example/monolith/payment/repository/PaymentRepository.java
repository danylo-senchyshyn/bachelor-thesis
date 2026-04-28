package com.example.monolith.payment.repository;

import com.example.monolith.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByReservationId(Long reservationId);
    List<Payment> findAllByReservationId(Long reservationId);
}
