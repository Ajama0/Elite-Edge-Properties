package com.example.Elite.Edge.Properties.service;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.dto.LeaseDto;
import com.example.Elite.Edge.Properties.dto.PaymentDto;
import com.example.Elite.Edge.Properties.exceptions.LeaseNotFoundException;
import com.example.Elite.Edge.Properties.exceptions.PaymentNotFoundException;
import com.example.Elite.Edge.Properties.exceptions.PropertyException;
import com.example.Elite.Edge.Properties.exceptions.UnitException;
import com.example.Elite.Edge.Properties.mapper.LeaseMapper;
import com.example.Elite.Edge.Properties.model.Lease;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.repository.LeaseRepository;
import com.example.Elite.Edge.Properties.repository.PropertyRepository;
import com.example.Elite.Edge.Properties.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;

    private final UnitRepository unitRepository;

    public LeaseService(LeaseRepository leaseRepository,
                        PropertyRepository propertyRepository,
                        UnitRepository unitRepository){
        this.leaseRepository = leaseRepository;
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;

    }

    @Transactional

    public LeaseMapper fetchAllLeases(Long propertyId, Long unitId, int pageNo, int pageSize){
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(()-> new PropertyException("property does not exist"));


        Units unit = property.getUnits()
                .stream()
                .filter(units -> units.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() ->new UnitException("unit" + unitId + " was not found"));


        /**
         * for pagination to work effectively we need to return lease records based on their unit id
         * one unit could have 1000s of leases, hence we'd like to optimize queries and return a subset
         */

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Lease> fetchAll = leaseRepository.findLeasesByPropertyAndUnit(propertyId, unit.getId(),
                pageable);
        //get the content of the page and map it to a lease dto

        if (fetchAll.isEmpty()){
            throw new LeaseNotFoundException("There are currently no available leases");
        }

        List<LeaseDto> leaseDtos = fetchAll.getContent()
                .stream()
                .map(lease -> new LeaseDto(lease))
                .collect(Collectors.toList());

        LeaseMapper leases = new LeaseMapper(
                leaseDtos,
                fetchAll.getNumber(),
                fetchAll.getSize(),
                fetchAll.getTotalPages(),
                fetchAll.getTotalElements(),
                fetchAll.isLast()

        );

        return leases;

    }


    public List<PaymentDto> fetchLeasePayment(Long leaseId) {
        //validate lease is present
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(()-> new LeaseNotFoundException("Lease with id " + leaseId + " does not exist"));


        List<PaymentDto> paymentDtoList = Optional.ofNullable(lease.getPayments())
                .orElseThrow(()->new PaymentNotFoundException("There are no payments for this lease"))
                .stream()
                .map(PaymentDto::new)
                .toList();

        if(paymentDtoList.isEmpty()){
            throw new PaymentNotFoundException("There are no payments for this lease");
        }
        return paymentDtoList;
    }

    @Transactional
    public Object fetchTenantLeaseByUnit(Long unitId, Long tenantId) {
        //ensure tenant exists, ensure unit exists, validate params
        //if the client passes in both the unit and tenant, then the specific lease is returned
       if(unitId!=null && tenantId!=null) {
           Lease lease= leaseRepository.leaseByUnitAndTenant(unitId, tenantId);
           return new LeaseDto(lease);

       }else if (tenantId==null && unitId!=null) {
           // here we assume the client only includes the unit, hence they want all the leases of a unit
           return leaseRepository.findAll()
                   .stream()
                   .filter(lease -> lease.getUnit().getId().equals(unitId))
                   .map(LeaseDto::new)
                   .toList();

       }else if(tenantId!=null) {//this means the tenant id has been provided and we can return the lease associated to a tenant

           return leaseRepository.findAll()
                   .stream()
                   .filter(lease -> lease.getTenants().getId().equals(tenantId))
                   .map(LeaseDto::new)
                   .findFirst()
                   .orElseThrow(() -> new LeaseNotFoundException("Lease for tenant: " + tenantId + " does not exist"));
       }

       else{
           return new LeaseNotFoundException("Please provide either a valid unit ID, tenant ID, or both") ;

       }

    }

    public List<LeaseDto> activeLeases(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(()-> new PropertyException("property with id: " + propertyId + " does not exist"));

        List<Units> units = property.getUnits();
        List<LeaseDto> propertyLeases =  units.
                stream()
                .flatMap(units1 -> units1.getLease().stream())
                .filter(leases -> leases.getStatus().equals(Status.ACTIVE))
                .map(LeaseDto::new)
                .toList();


        return propertyLeases;



    }
}
