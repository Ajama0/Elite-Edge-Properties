package com.example.Elite.Edge.Properties.controller;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.dto.RequestTenantDto;
import com.example.Elite.Edge.Properties.dto.ResponseTenantDto;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.service.TenantsService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tenants")
public class TenantsController {

    private final TenantsService tenantsService;


    public TenantsController(TenantsService tenantsService){
        this.tenantsService = tenantsService;
    }


    //hide internals with DTO when retrieving tenants
    //when fetching tenants, we can filter them by property but also include their unit

    @GetMapping("All")
    public ResponseEntity<ApiResponse<List<ResponseTenantDto>>> fetchTenants(
            @RequestParam("property_id") Long propertyId

    ){
        List<ResponseTenantDto> responseTenantDtoList = tenantsService.fetchAllTenants(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("success", responseTenantDtoList));


    }

    //fetch Tenants based off their status(Active, Deleted) etc


    @GetMapping("by/status")
    public ResponseEntity<ApiResponse<Object>>fetchByStatus(
            @RequestParam("property_id") Long propertyId,
            @RequestParam("status")Status status
            ){
        List<ResponseTenantDto> Tenants= tenantsService.retrieveByStatus(propertyId, status);
        return new ResponseEntity<>(new ApiResponse<>("Success",Tenants ),
                HttpStatus.OK);

    }

    @GetMapping(path = "retrieve/by/occupation")
    public ResponseEntity<ApiResponse<Object>> fetchByOccupation(
            @RequestParam("property_id") Long propertyId,
            @RequestParam("Occupation") String Occupation
    ){
        List<ResponseTenantDto> tenants = tenantsService.findByOccupation(propertyId, Occupation);
        return new ResponseEntity<>(new ApiResponse<>("success", tenants),
                HttpStatus.OK);


    }



    @GetMapping(path = "{id}/unit")
    public ResponseEntity<ApiResponse<Units>> findTenantApartment(
            @PathVariable("id") Long tenantId
    ){
        Units unit = tenantsService.findTenantApt(tenantId);
        return ResponseEntity.ok(new ApiResponse<>("success", unit));
    }



    //Best practice is to typically return the id of a POST request
    @PostMapping(path = "create")
    public ResponseEntity<ApiResponse<Long>> createTenant(
            @RequestBody RequestTenantDto requestTenantDto,
            @RequestParam("unit") Long unitId
    ){
        Long tenantId = tenantsService.createTenant(requestTenantDto, unitId);
        return new ResponseEntity<>(new ApiResponse<>("success", tenantId),
                HttpStatus.CREATED);
    }








}
