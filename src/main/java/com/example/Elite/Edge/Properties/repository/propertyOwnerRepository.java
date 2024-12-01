package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.PropertyOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface propertyOwnerRepository extends JpaRepository<PropertyOwner,Long> {
}
