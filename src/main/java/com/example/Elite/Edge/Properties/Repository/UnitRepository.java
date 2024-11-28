package com.example.Elite.Edge.Properties.Repository;

import com.example.Elite.Edge.Properties.Model.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Units,Long> {
}
