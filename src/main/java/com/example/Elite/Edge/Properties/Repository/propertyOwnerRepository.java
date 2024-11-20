package com.example.Elite.Edge.Properties.Repository;

import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface propertyOwnerRepository extends JpaRepository<PropertyOwner,Long> {
}
