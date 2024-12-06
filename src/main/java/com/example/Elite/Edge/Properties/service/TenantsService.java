package com.example.Elite.Edge.Properties.service;

import com.example.Elite.Edge.Properties.constants.Status;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TenantsService {


    private final TenantRepository tenantRepository;
    private final PropertyRepository propertyRepository;

    public TenantsService(TenantRepository tenantRepository, PropertyRepository propertyRepository){
        this.tenantRepository = tenantRepository;
        this.propertyRepository = propertyRepository;
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
}
