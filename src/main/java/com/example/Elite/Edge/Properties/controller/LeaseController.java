package com.example.Elite.Edge.Properties.controller;


import com.example.Elite.Edge.Properties.dto.LeaseDto;
import com.example.Elite.Edge.Properties.mapper.LeaseMapper;
import com.example.Elite.Edge.Properties.service.LeaseService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lease")
public class LeaseController {


    private final LeaseService leaseService;

    public LeaseController(LeaseService leaseService){
        this.leaseService = leaseService;
    }


    //Add pagination for retrieving leases for a unit


    @GetMapping("/All")
    public ResponseEntity<ApiResponse<?>> fetchAllLeases(
            @RequestParam(name = "property_id") Long propertyId,
            @RequestParam(name = "unit_id") Long unitId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        LeaseMapper leases = leaseService.fetchAllLeases(propertyId, unitId, pageNo, pageSize);
        return new ResponseEntity<>(new ApiResponse<>("success", leases), HttpStatus.OK);

    }

}
