package com.example.monolith.notification.repository;

import com.example.monolith.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    boolean existsByPaymentId(Long paymentId);

    boolean existsByPaymentIdAndEventType(Long paymentId, String eventType);
}
