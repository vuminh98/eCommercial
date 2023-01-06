package com.example.comercial.repository;

import com.example.comercial.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment,Long> {
    @Transactional
    Optional<Payment> findByUserIdAndStoreId(Long userId, Long storeId);
    Iterable<Payment> findAllByUserId(long userId);
}
