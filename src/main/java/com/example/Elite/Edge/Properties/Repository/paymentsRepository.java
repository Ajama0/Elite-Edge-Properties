package com.example.Elite.Edge.Properties.Repository;

import com.example.Elite.Edge.Properties.Model.Payments;
import com.example.Elite.Edge.Properties.Model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface paymentsRepository extends JpaRepository<Payments,Long> {
}
