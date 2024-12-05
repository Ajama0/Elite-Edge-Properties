package com.example.Elite.Edge.Properties.service;


import com.example.Elite.Edge.Properties.dto.LeaseDto;
import com.example.Elite.Edge.Properties.exceptions.LeaseNotFoundException;
import com.example.Elite.Edge.Properties.exceptions.UnitException;
import com.example.Elite.Edge.Properties.mapper.LeaseMapper;
import com.example.Elite.Edge.Properties.model.Lease;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.repository.LeaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaseService {

    private final LeaseRepository leaseRepository;

    @Autowired
    private UnitService unitService;

    public LeaseService(LeaseRepository leaseRepository){
        this.leaseRepository = leaseRepository;
    }

    public LeaseMapper fetchAllLeases(Long propertyId, Long unitId, int pageNo, int pageSize){
        Property property = unitService.propertyExists(propertyId);


        Units unit = property.getUnits()
                .stream()
                .filter(units -> units.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() ->new UnitException(unitId + "was not found"));


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
}
