package com.example.Elite.Edge.Properties.Repository;


import com.example.Elite.Edge.Properties.Model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface propertyRepository extends JpaRepository<Property,Long> {


    @Query("SELECT s FROM Property s WHERE s.propertyname=?1 and s.address=?2")
    Optional<Property> findByPropertyNameAndAddress(String propertyname,String address);

}
