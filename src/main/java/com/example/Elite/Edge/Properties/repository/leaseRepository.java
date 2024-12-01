package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface leaseRepository extends JpaRepository<Lease,Long> {
}
