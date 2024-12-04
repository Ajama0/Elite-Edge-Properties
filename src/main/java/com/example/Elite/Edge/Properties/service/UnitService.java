package com.example.Elite.Edge.Properties.service;


import com.example.Elite.Edge.Properties.dto.ResponseTenantDto;
import com.example.Elite.Edge.Properties.dto.UnitDto;
import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.constants.unitStatus;
import com.example.Elite.Edge.Properties.constants.unitType;
import com.example.Elite.Edge.Properties.exceptions.PropertyException;
import com.example.Elite.Edge.Properties.exceptions.UnitException;
import com.example.Elite.Edge.Properties.exceptions.tenantNotFoundException;
import com.example.Elite.Edge.Properties.mapper.TenantMapper;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.repository.PropertyRepository;
import com.example.Elite.Edge.Properties.repository.UnitRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;

    private static final Logger logger = LoggerFactory.getLogger(UnitService.class);
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

    @Transactional
    public ResponseTenantDto fetchTenant(Long propertyId, Long unitId){
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
        if(tenants == null){
            throw new tenantNotFoundException("tenant does not exist");
        }

        return TenantMapper.mapTenantsForResponse(tenants);


    }

    // TODO revisit this function and allow unitType setting for the client
    //unit type returns null when parsed into the request body
    @Transactional
    public Long createUnit(Long id, UnitDto unitDto) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyException("Property with id " + id + " does not exist"));


        //we now know the property exists
        // map the dto to a property, save it and return the dto.

        //ensure the unit doesn't already exist
        Optional<Units> unitDuplicationCheck = property.getUnits()
                .stream()
                .filter(units -> units.getUnitNumber().equals(unitDto.getUnitNumber()))
                .findFirst();

        if (unitDuplicationCheck.isPresent()) {
            throw new UnitException("unit " + unitDto.getUnitNumber() + " already exists");
        }


        Units unit = new Units(unitDto.getUnitNumber(),
                unitDto.getFloorNumber(),
                unitDto.getRentprice(),
                unitDto.getUnitvalue(),
                unitDto.getNumberofrooms(),
                unitDto.getDeposit()
                );

        unit.setUnitStatus(unitStatus.VACANT);
        unit.setUnitType(unitType.APARTMENT);
        property.getUnits().add(unit);
        unit.setProperty(property);


        unitRepository.save(unit);


        //best practice is to return a UUID or the identifier of the created resource

        return unit.getId();

    }

    //helper function to continuously check that property is exists

    private  Property propertyExists(Long id){
        return propertyRepository.findById(id).orElseThrow(()-> new PropertyException("Property does not exist"));
    }
    public List<Units> fetchByNoOfRooms(Long id, Integer noOfRooms) {
        //if this returns a property then a property does exist
        Property property = propertyExists(id);


        List<Units> propertyUnits = property.getUnits()
                .stream()
                .filter(units -> units.getNumberofrooms().equals(noOfRooms))
                .collect(Collectors.toList());

        if(propertyUnits.isEmpty()){
            throw new UnitException("There are no units with " + noOfRooms + " rooms");
        }

        return propertyUnits;

    }


    public Units updateRentPrice(Long propertyId, Long unitId, Double price) {
        //call the property function, to ensure property exists
        Property property = propertyExists(propertyId);

        //get the specific unit for the property
        Units unit = property.getUnits()
                .stream()
                .filter(units1 -> units1.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() -> new UnitException(unitId + "does not exist"));

        //let's ensure the price being entered by the client is not the same price already saved
        if(price.equals(unit.getRentprice())){
            throw new UnitException("Please enter a different rent price");

        }
        unit.setRentprice(price);
        unitRepository.save(unit);

        return unit;

    }


    @Transactional
    public Units updateStatus(Long propertyId, Long unitId, unitStatus status) {
        propertyExists(propertyId);

        //fetch unit for the property
        Units unit = unitRepository.findByPropertyIdAndUnit(propertyId,unitId);

        //remove redundancy
        if(unit.getUnitStatus().equals(status)){
            throw new UnitException(status + "is already set for the unit");
        }

        if(status.equals(unitStatus.VACANT)){
            //we'd like to disassociate the tenants automatically associated to that unit
            unit.getTenant().setTenantStatus(Status.DELETED);
            unit.setTenant(null);

        }

        unit.setUnitStatus(status);
        unitRepository.save(unit);

        return unit;

    }



}
