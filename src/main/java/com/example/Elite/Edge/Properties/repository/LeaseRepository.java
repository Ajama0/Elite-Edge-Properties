package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Lease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaseRepository extends JpaRepository<Lease,Long> {


    @Query("SELECT l FROM Lease l WHERE l.unit.property.Id = :propertyId AND l.unit.id = :unitId")
    Page<Lease> findLeasesByPropertyAndUnit(@Param("propertyId") Long propertyId,
                                            @Param("unitId") Long unitId,
                                            Pageable pageable);


    @Query("SELECT l from Lease l WHERE  l.unit.id=:id1 AND l.tenants.id=:id2")
    Lease leaseByUnitAndTenant(@Param("id1")Long unitId, @Param("id2") Long tenantId);
}
