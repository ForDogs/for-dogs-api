package com.fordogs.payment.infrastructure;

import com.fordogs.payment.domain.entity.PaymentCancelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentCancelRepository extends JpaRepository<PaymentCancelEntity, UUID> {
}
