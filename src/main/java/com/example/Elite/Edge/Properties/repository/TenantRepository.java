package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Tenants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenants,Long> {

    @Query("SELECT t FROM Tenants t WHERE t.units.property.Id =:propertyId ")
    List<Tenants> findTenantsByProperty(@Param("propertyId") Long propertyId);

    Optional<Tenants> findByEmail(String email);
}

