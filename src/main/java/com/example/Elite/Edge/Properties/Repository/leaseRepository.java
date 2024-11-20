package com.example.Elite.Edge.Properties.Repository;

import com.example.Elite.Edge.Properties.Model.Lease;
import com.example.Elite.Edge.Properties.Model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface leaseRepository extends JpaRepository<Lease,Long> {
}
