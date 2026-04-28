package com.example.monolith.notification.service;

import com.example.monolith.notification.entity.NotificationLog;
import com.example.monolith.notification.repository.NotificationLogRepository;
import com.example.monolith.payment.controller.dto.PaymentConfirmedRequest;
import com.example.monolith.payment.controller.dto.PaymentFailedRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository notificationLogRepository;

    @Transactional
    public void paymentConfirmed(Long paymentId) {
        saveIfNotExists(paymentId, "PAYMENT_CONFIRMED");
    }

    @Transactional
    public void paymentFailed(Long paymentId) {
        saveIfNotExists(paymentId, "PAYMENT_FAILED");
    }

    private void saveIfNotExists(Long paymentId, String eventType) {
        if (notificationLogRepository.existsByPaymentIdAndEventType(paymentId, eventType)) {
            return;
        }

        notificationLogRepository.save(
                NotificationLog.builder()
                        .paymentId(paymentId)
                        .eventType(eventType)
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}