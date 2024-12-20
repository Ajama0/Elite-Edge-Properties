package com.example.Elite.Edge.Properties.repository;


import com.example.Elite.Edge.Properties.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {


    @Query("SELECT s FROM Property s WHERE s.propertyname=?1 and s.address=?2")
    Optional<Property> findByPropertyNameAndAddress(String propertyname,String address);

}
