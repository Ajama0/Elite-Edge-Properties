package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payments,Long> {
}
