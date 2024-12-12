package com.example.Elite.Edge.Properties.service;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.constants.UnitStatus;
import com.example.Elite.Edge.Properties.dto.LeaseDto;
import com.example.Elite.Edge.Properties.dto.LeaseRequestDto;
import com.example.Elite.Edge.Properties.dto.PaymentDto;
import com.example.Elite.Edge.Properties.exceptions.*;
import com.example.Elite.Edge.Properties.mapper.LeaseMapper;
import com.example.Elite.Edge.Properties.model.Lease;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.repository.LeaseRepository;
import com.example.Elite.Edge.Properties.repository.PropertyRepository;
import com.example.Elite.Edge.Properties.repository.TenantRepository;
import com.example.Elite.Edge.Properties.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.server.PortInUseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;

    private final TenantRepository tenantRepository;

    public LeaseService(LeaseRepository leaseRepository,
                        PropertyRepository propertyRepository,
                        TenantRepository tenantRepository) {
        this.leaseRepository = leaseRepository;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;

    }

    @Transactional

    public LeaseMapper fetchAllLeases(Long propertyId, Long unitId, int pageNo, int pageSize) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyException("property does not exist"));


        Units unit = property.getUnits()
                .stream()
                .filter(units -> units.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() -> new UnitException("unit" + unitId + " was not found"));


        /**
         * for pagination to work effectively we need to return lease records based on their unit id
         * one unit could have 1000s of leases, hence we'd like to optimize queries and return a subset
         */

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Lease> fetchAll = leaseRepository.findLeasesByPropertyAndUnit(propertyId, unit.getId(),
                pageable);
        //get the content of the page and map it to a lease dto

        if (fetchAll.isEmpty()) {
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
                .orElseThrow(() -> new LeaseNotFoundException("Lease with id " + leaseId + " does not exist"));


        List<PaymentDto> paymentDtoList = Optional.ofNullable(lease.getPayments())
                .orElseThrow(() -> new PaymentNotFoundException("There are no payments for this lease"))
                .stream()
                .map(PaymentDto::new)
                .toList();

        if (paymentDtoList.isEmpty()) {
            throw new PaymentNotFoundException("There are no payments for this lease");
        }
        return paymentDtoList;
    }

    @Transactional
    public Object fetchTenantLeaseByUnit(Long unitId, Long tenantId) {
        //ensure tenant exists, ensure unit exists, validate params
        //if the client passes in both the unit and tenant, then the specific lease is returned
        if (unitId != null && tenantId != null) {
            Lease lease = leaseRepository.leaseByUnitAndTenant(unitId, tenantId);
            return new LeaseDto(lease);

        } else if (tenantId == null && unitId != null) {
            // here we assume the client only includes the unit, hence they want all the leases of a unit
            return leaseRepository.findAll()
                    .stream()
                    .filter(lease -> lease.getUnit().getId().equals(unitId))
                    .map(LeaseDto::new)
                    .toList();

        } else if (tenantId != null) {//this means the tenant id has been provided and we can return the lease associated to a tenant

            return leaseRepository.findAll()
                    .stream()
                    .filter(lease -> lease.getTenants().getId().equals(tenantId))
                    .map(LeaseDto::new)
                    .findFirst()
                    .orElseThrow(() -> new LeaseNotFoundException("Lease for tenant: " + tenantId + " does not exist"));
        } else {
            return new LeaseNotFoundException("Please provide either a valid unit ID, tenant ID, or both");

        }

    }

    public List<LeaseDto> activeLeases(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyException("property with id: " + propertyId + " does not exist"));

        List<Units> units = property.getUnits();
        List<LeaseDto> propertyLeases = units.
                stream()
                .flatMap(units1 -> units1.getLease().stream())
                .filter(leases -> leases.getStatus().equals(Status.ACTIVE))
                .map(LeaseDto::new)
                .toList();


        return propertyLeases;

    }

    @Transactional

    public List<LeaseDto> leasesByStatus(Long propertyId, Long unitId, Status status) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyException("please enter a valid property"));

        Units unit = property.getUnits().stream()
                .filter(units1 -> units1.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() -> new UnitException("unit with id: " + unitId + " was not found"));

        List<LeaseDto> leaseStatuses = Optional.ofNullable(unit
                        .getLease())
                .orElseThrow(() -> new LeaseNotFoundException("there are no leases for this unit"))
                .stream()
                .filter(lease -> lease.getStatus().equals(status))
                .map(lease -> new LeaseDto(lease))
                .collect(Collectors.toList());

        if (leaseStatuses.isEmpty()) {
            throw new LeaseNotFoundException(("There are no leases for this unit"));
        }

        return leaseStatuses;

    }

    @Transactional
    public List<LeaseDto> fetchAllExpiringLeases(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyException("property with id : " + propertyId + " does not exist"));

        List<Units> unitsList = property.getUnits();
        //for every lease associated to a unit that is active, we want to return the current expiring leases
        List<Lease> leases = leaseRepository.findLeasesByProperty(property.getId());

        //filter the dates of each lease to the ones that expire in 10 days from the current date
        List<LeaseDto> leaseDtos = leases
                .stream()
                .filter(lease -> lease.getEndDate().minusDays(10).equals(LocalDate.now()))
                .map(LeaseDto::new)
                .collect(Collectors.toList());

        return leaseDtos;

    }

    @Transactional
    public Long createLease(Long propertyId, Long unitId, Long tenantId, LeaseRequestDto lease) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyException("property with id: " + propertyId + " does not exist"));


        Units unit = property.getUnits().stream()
                .filter(units -> units.getId().equals(unitId))
                .findAny()
                .orElseThrow(() -> new UnitException("No found units within property: " + property.getPropertyname() + " "));

        Tenants tenants = tenantRepository.findById(tenantId)
                .filter(tenants1 -> tenants1.getUnit().getId().equals(unit.getId()))
                .orElseThrow(() -> new TenantNotFoundException("tenant with id : " + tenantId + " does not exist"));


        Lease persistedLease = new Lease(
                LocalDate.now(),
                lease.getEndDate(),
                lease.getRentAmount(),
                lease.getDepositAmount(),
                lease.getTerminationNoticePeriod(),
                lease.getAgreementDocument(),
                lease.getStatus()
        );

        persistedLease.setUnit(unit);
        persistedLease.setTenants(tenants);
        //everytime we create a lease, that unit will always be occupied at that time
        unit.setUnitStatus(UnitStatus.OCCUPIED);
        leaseRepository.save(persistedLease);
        //best practice is to return Id's from POST requests
        return persistedLease.getId();

    }


    public LeaseDto updateStatus(Long leaseId, Status status) {
        Lease fetchLease = leaseRepository.findById(leaseId)
                .orElseThrow(()-> new LeaseNotFoundException("lease with id: " + leaseId + " does not exist"));

        if(fetchLease.getStatus().equals(status)){
            throw new IllegalArgumentException("Please select a different option");
        }
        fetchLease.setStatus(status);
        Lease lease = leaseRepository.save(fetchLease);

        return new LeaseDto(lease);
    }


    public LeaseDto updateDate(Long leaseId, LocalDate endingDate) {
        Lease findLease = leaseRepository.findById(leaseId)
                .orElseThrow(()-> new LeaseNotFoundException("lease with id: " + leaseId + " does not exist"));

        if(endingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("please enter a valid date");

        }if(findLease.getEndDate().equals(endingDate))  {
            throw new IllegalArgumentException("The date must be different to the one set for the current lease");
        }

        if (!findLease.getStatus().equals(Status.ACTIVE)) {
            throw new IllegalArgumentException("Cannot update the end date for a non-active lease.");
            }

        findLease.setEndDate(endingDate);
        Lease saveLease = leaseRepository.save(findLease);
        return new LeaseDto(saveLease);

        }


    @Transactional
    public LeaseDto updateRentAmount(Long leaseId, Double rentAmount) {
        // Validate the lease exists
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new LeaseNotFoundException("Lease with id: " + leaseId + " does not exist"));

        // Validate the new rent amount
        if (rentAmount == null || rentAmount <= 0) {
            throw new IllegalArgumentException("Please provide a valid rent amount greater than 0");
        }

        // Avoid unnecessary updates
        if (lease.getRentAmount().equals(rentAmount)) {
            throw new IllegalArgumentException("The new rent amount is the same as the current rent amount");
        }

        lease.setRentAmount(rentAmount);
        leaseRepository.save(lease);
        return new LeaseDto(lease);
    }

}
