package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Units,Long> {
}
