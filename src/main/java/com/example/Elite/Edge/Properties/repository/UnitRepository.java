package com.example.Elite.Edge.Properties.repository;

import com.example.Elite.Edge.Properties.model.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Units,Long> {


}
