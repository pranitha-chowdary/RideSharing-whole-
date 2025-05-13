package com.rideshare.repository;

import com.rideshare.entity.Payment;
import com.rideshare.entity.Ride;
import com.rideshare.util.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRide(Ride ride);

    Optional<Payment> findByQrCodeId(String qrCodeId);

    List<Payment> findByPaymentStatus(PaymentStatus status);
}