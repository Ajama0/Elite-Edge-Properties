package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Tenants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface tenantsRepository extends JpaRepository<Tenants,Long> {
}
