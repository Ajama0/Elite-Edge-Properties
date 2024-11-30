package com.example.Elite.Edge.Properties.Service;


import com.example.Elite.Edge.Properties.DTO.TenantDto;
import com.example.Elite.Edge.Properties.Enums.Status;
import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Enums.unitType;
import com.example.Elite.Edge.Properties.Exceptions.PropertyException;
import com.example.Elite.Edge.Properties.Exceptions.UnitException;
import com.example.Elite.Edge.Properties.Exceptions.tenantNotFoundException;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.Tenants;
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

        //ensure the property being returned is not archived and is an available property

        if(!property.getStatus().equals(Status.ACTIVE)){
            throw new PropertyException("Archived properties are not for viewings.");
        }

        List<Units> propertyUnits = property.getUnits().stream()
                .filter(units -> units.getRentprice()>=min && units.getRentprice()<=max)
                .collect(Collectors.toList());

        if(propertyUnits.isEmpty()){
            throw new UnitException("There are no properties within this range");
        }

        return propertyUnits;

    }

    public List<Units> retrieveByType(Long id, unitType type){
        //check if the property exist first
        Property validateProperty = propertyRepository.findById(id)
                .orElseThrow(()->  new PropertyException("Property does not exist"));

        //fetch the units matching to the status type of the apartment

        List<Units> unitTypes = validateProperty.getUnits()
                .stream()
                .filter(units -> units.getUnitType().equals(type))
                .collect(Collectors.toList());


        return unitTypes;
    }

    public TenantDto fetchTenant(Long propertyId, Long unitId){
        Property validateProperty = propertyRepository.findById(propertyId)
                .orElseThrow(()->  new PropertyException("Property does not exist"));

        //now for that property get the specific unit with id = 1;

        Units fetchUnit = validateProperty.getUnits()
                .stream()
                .filter(units -> units.getId().equals(unitId))
                .findFirst()
                .orElseThrow(()-> new UnitException("unit id " + unitId + " does not exist for this property"));

        //the optional is unwrapped by OrElseThrow is value is present, we then get the tenant associated with the unit
        Tenants tenants = fetchUnit.getTenant();

        return new TenantDto(tenants);


    }
}
