package com.github.sujankumarmitra.springstatemachinedemo.repository;

import com.github.sujankumarmitra.springstatemachinedemo.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}
