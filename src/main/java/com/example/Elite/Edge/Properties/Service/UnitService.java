package com.example.Elite.Edge.Properties.Service;


import com.example.Elite.Edge.Properties.Enums.Status;
import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Exceptions.PropertyException;
import com.example.Elite.Edge.Properties.Exceptions.UnitException;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Repository.PropertyRepository;
import com.example.Elite.Edge.Properties.Repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;

    public UnitService(UnitRepository unitRepository, PropertyRepository propertyRepository){
        this.unitRepository = unitRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<Units> retrieveUnits() {
        List<Units> fetchUnits = unitRepository.findAll();

        if(fetchUnits.isEmpty()){
            throw new UnitException("no unit currently exists");
        }
        return fetchUnits;
    }

    public List<Units> fetchArchivedUnits() {

        List<Units> archivedUnits = unitRepository.findAll()
                .stream()
                .filter(units-> units.getUnitStatus().equals(unitStatus.ARCHIVED))
                .collect(Collectors.toList());

        if(archivedUnits.isEmpty()){
            throw new UnitException("no archived records found");
        }
        return archivedUnits;
    }

    @Transactional
    public List<Units> retrievePropertyUnits(Long id) {
        //ensure the property we fetch is not deleted
        Property fetchProperty = propertyRepository.findById(id)
                .orElseThrow(()-> new PropertyException("property does not exist"));

        if(fetchProperty.getStatus().equals(Status.DELETED)){
            throw new PropertyException("Please choose an appropriate property");
        }
        //only retrieve units that haven't been soft deleted
        List<Units> associatedUnits = fetchProperty.getUnits().
                stream().
                filter(units -> !units.getUnitStatus().equals(unitStatus.ARCHIVED)).
                toList();

        if(associatedUnits.isEmpty()){
            throw new PropertyException("units do not exist");
        }

        return associatedUnits;
        }


    public List<Units> retrieveUnitsInRange(Long id, double min, double max) {
        //check if property exists first
        Property property = propertyRepository.findById(id)
                .orElseThrow(()-> new PropertyException("INVALID PROPERTY"));

        List<Units> propertyUnits = property.getUnits().stream()
                .filter(units -> units.getRentprice()>=min && units.getRentprice()<=max)
                .collect(Collectors.toList());

        if(propertyUnits.isEmpty()){
            throw new UnitException("There are no properties within this range");
        }

        return propertyUnits;

    }
}
