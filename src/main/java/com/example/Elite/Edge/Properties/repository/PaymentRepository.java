package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Payments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payments,Long> {


    @Query("Select p from Payments p where p.lease.unit.property.Id=:propertyId")
    Page<Payments> findPaymentsByPropertyAndStatus(@Param("propertyId") Long id, Pageable pageable);
}
