package com.example.Elite.Edge.Properties.service;

import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.constants.unitStatus;
import com.example.Elite.Edge.Properties.dto.RequestTenantDto;
import com.example.Elite.Edge.Properties.dto.ResponseTenantDto;
import com.example.Elite.Edge.Properties.exceptions.PropertyException;
import com.example.Elite.Edge.Properties.exceptions.TenantNotFoundException;
import com.example.Elite.Edge.Properties.exceptions.UnitException;
import com.example.Elite.Edge.Properties.mapper.TenantMapper;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.repository.PropertyRepository;
import com.example.Elite.Edge.Properties.repository.TenantRepository;
import com.example.Elite.Edge.Properties.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantsService {


    private final TenantRepository tenantRepository;
    private final PropertyRepository propertyRepository;

    private final UnitRepository unitRepository;

    public TenantsService(TenantRepository tenantRepository, PropertyRepository propertyRepository,
                          UnitRepository unitRepository){
        this.tenantRepository = tenantRepository;
        this.propertyRepository = propertyRepository;
        this.unitRepository=unitRepository;
    }


    public List<ResponseTenantDto> fetchAllTenants(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(()-> new PropertyException(propertyId + " does not exist"));

        List<Tenants> fetchTenants = tenantRepository.findTenantsByProperty(property.getId());
        /**
         * for instance, we pass in property 1, we want to return all the tenants that are present within
         * a property
         */

        //ensure a property has Tenants
        if(fetchTenants.isEmpty()){
            throw new TenantNotFoundException("There are currently no Tenants for property "
            + propertyId);
        }

        //convert the fetched tenants into a requestTenantDto to hide internals
        List<ResponseTenantDto> responseTenantDtoList = fetchTenants
                .stream()
                .map(TenantMapper::mapTenantsForResponse)
                .toList();

        return responseTenantDtoList;

    }
    public List<ResponseTenantDto> retrieveByStatus(Long propertyId, Status status){
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(()-> new PropertyException(propertyId + " does not exist"));

        //now fetch the tenants from the property
       List<Units> unit =  property.getUnits();

        /**
         * Loop through the units associated to a property and fetch the tenants
         * This is based on the status passed by the client
         * Finally we return a DTO to hide internals
         */
        return unit
               .stream()
               .map(Units::getTenant)
               .filter(tenants -> tenants!=null && tenants.getTenantStatus().equals(status))
               .map(TenantMapper::mapTenantsForResponse)
               .toList();

    }


    @Transactional
    public List<ResponseTenantDto> findByOccupation(Long propertyId, String occupation) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(()-> new PropertyException("Property does not exist"));


        List<ResponseTenantDto> tenantOccupationList = property.getUnits()
                .stream()
                .map(Units::getTenant)
                .filter(tenants -> tenants.getOccupation().equals(occupation))
                .map(tenants -> new ResponseTenantDto(
                        tenants.getId(),
                        tenants.getFirstName(),
                        tenants.getOccupation()))
                .collect(Collectors.toList());

        return tenantOccupationList;


    }

    public Units findTenantApt(Long tenantId) {

        Tenants tenants = tenantRepository.findById(tenantId)
                .orElseThrow(()-> new TenantNotFoundException("Tenant does not exist... Please " +
                        "try again!"));


        Units unit = Optional.ofNullable(tenants.getUnit())
                .orElseThrow(()-> new UnitException("There are not units associated to the Tenant with id"
                        + tenants.getId() + "."));

        return unit;
    }

    @Transactional
    public Long createTenant(RequestTenantDto requestTenantDto, Long unitId ) {
        //let's first ensure that the tenant does not already exist in our database
        Optional<Tenants> tenant = tenantRepository.findByEmail(requestTenantDto.getEmail());

        if(tenant.isPresent()){
            throw new RuntimeException("The tenant already exists!");
        }
        //check if the unit the tenant wants is exists and is vacant

        Units unitExists = unitRepository.findById(unitId)
                .orElseThrow(()-> new UnitException("This unit does not exist"));
        if(!unitExists.getUnitStatus().equals(unitStatus.VACANT)){
            throw new UnitException("please select a vacant unit.");
        }

        //lets map our tenant instance using our mapper class
        Tenants tenant1 = TenantMapper.mapRequestToTenants(requestTenantDto);
        //change tenant address to their new apartment(unit)
        tenant1.setAddress(unitExists.getProperty().getAddress());
        //assign this Tenant to a unit. The client should also add the unit we associate the tenant to
        tenant1.setUnit(unitExists);
        unitExists.setUnitStatus(unitStatus.OCCUPIED);


        tenantRepository.save(tenant1);
        return tenant1.getId();

    }

    @Transactional
    public ResponseTenantDto updateEmail(String email, RequestTenantDto requestTenantDto) {
        /**
         * retrieve the tenant email from the request body,
         * validate in the DB that the tenant already exists
         * ensure tenant is a valid tenant otherwise reject the request as account was closed
         * retrieve tenant obj, and persist it into db with updated email
         *
         */

        Optional<Tenants> retrieveTenant = tenantRepository.findByEmail(requestTenantDto.getEmail());

        if(retrieveTenant.isEmpty()){
            throw new TenantNotFoundException("There is no account matching your request");
        }

        //ensure the tenant is an active tenant within our database

        Tenants fetchTenant = retrieveTenant.get();

        if(fetchTenant.getTenantStatus().equals(Status.DELETED)){
            throw new TenantNotFoundException("This tenants credentials cannot be updated... please sign up again");
        }
        //ensure email is not duplicative
        if(fetchTenant.getEmail().equals(email)){
            throw new IllegalArgumentException("Please enter a different email address.");
        }

        //if all passes we can save the tenant
        fetchTenant.setEmail(email);

        tenantRepository.save(fetchTenant);

        return TenantMapper.mapTenantsForResponse(fetchTenant);

    }

    public ResponseTenantDto updateIncome(RequestTenantDto requestTenantDto, double income) {
        Optional<Tenants> validateTenant = tenantRepository.findByEmail(requestTenantDto.getEmail());

        if(validateTenant.isEmpty()){
            throw new TenantNotFoundException("There is no user matching your credentials. Please sign up");
        }

        Tenants tenant = validateTenant.get();
        if(tenant.getTenantStatus().equals(Status.DELETED)){
            throw new TenantNotFoundException("your account has been closed. please create a new account");
        }

        if(income<=20000.00){
            throw new IllegalArgumentException("The selected income is to low");
        }

        tenant.setIncome(income);
        tenantRepository.save(tenant);
        ResponseTenantDto responseTenantDto = new ResponseTenantDto(tenant);
        return responseTenantDto;

    }
}
