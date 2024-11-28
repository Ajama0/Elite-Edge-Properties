package com.example.Elite.Edge.Properties.Service;


import com.example.Elite.Edge.Properties.Repository.UnitRepository;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }




}
