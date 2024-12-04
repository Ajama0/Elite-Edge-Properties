package com.example.Elite.Edge.Properties.controller;

import com.example.Elite.Edge.Properties.constants.unitStatus;
import com.example.Elite.Edge.Properties.dto.ResponseTenantDto;
import com.example.Elite.Edge.Properties.dto.UnitDto;
import com.example.Elite.Edge.Properties.constants.unitType;
import com.example.Elite.Edge.Properties.exceptions.UnitException;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.service.UnitService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/units")
public class UnitController {


    private final UnitService unitService;

    public UnitController(UnitService unitService){
        this.unitService = unitService;
    }
    @GetMapping(value = "all-units")
    public ResponseEntity<ApiResponse<List<Units>>> retrieveUnits(){
        try{
           List<Units> fetchUnits = unitService.retrieveUnits();
           return new ResponseEntity<>(new ApiResponse<>("success", fetchUnits), HttpStatus.OK);
        }catch(RuntimeException runtimeException){
            throw new UnitException("Units could not be found");
        }
    }

    //fetch Archive units - units that soft deleted due to property deletion
    //can only be done by an admin to recover for historical data
    @GetMapping(path = "archived/units")
    public ResponseEntity<Object> fetchArchivedUnits(){
        try{
            List<Units> archivedUnits = unitService.fetchArchivedUnits();
            return new ResponseEntity<>(archivedUnits, HttpStatus.OK);
        }catch(RuntimeException runtimeException){
            throw new UnitException("Archived properties could not be fetched");
        }

    }


    @GetMapping(path = "property/{id}/units")
    public ResponseEntity<ApiResponse<Object>> retrievePropertyUnits(@PathVariable("id")
                                                                     Long propertyId){
        try{
            List<Units> retrieveProperty = unitService.retrievePropertyUnits(propertyId);
            return new ResponseEntity<>(new ApiResponse<>("success", retrieveProperty),
                    HttpStatus.OK);
        }catch (RuntimeException runtimeException){
            throw new UnitException("units for property: " + propertyId + " were not found");
        }
    }

    @GetMapping(value = "units/price/range")
    public ResponseEntity<ApiResponse<Object>> fetchUnitsByPrice(
            @RequestParam("Property_id")Long id,
            @RequestParam("minimum_rent_price")double min,
            @RequestParam("maximum_rent_price")double max){
        try {
            List<Units> unitsInRange = unitService.retrieveUnitsInRange(id, min, max);
            return ResponseEntity.ok(new ApiResponse<>("success", unitsInRange));
        }catch (RuntimeException runtimeException){
            throw new UnitException("enter a different range");
        }

    }

    //retrieve all units from a property that are Apartment or studio depending on what the user inputs
    @GetMapping(value = "property/units/e/type")
    public ResponseEntity<ApiResponse<Object>> fetchUnitType(
            @RequestParam Long id,
            @RequestParam("unit_type") unitType Unittype
            ){
        try{
            List<Units> units = unitService.retrieveByType(id, Unittype);
            if(units.isEmpty()){
                throw new UnitException("No available units of the selected type at the moment.");
            }
            return new ResponseEntity<>(new ApiResponse<>("success", units), HttpStatus.OK);

        }catch (RuntimeException runtimeException){
            throw new UnitException("Ensure a valid and correct type was selected");
        }
    }

    @GetMapping(path = "tenants/{property_id}/{unit_id}")
    public ResponseEntity<ApiResponse<?>> fetchUnitTenants(
            @PathVariable("property_id")Long propertyId,
            @PathVariable("unit_id") Long unitId){

        ResponseTenantDto unitTenants = unitService.fetchTenant(propertyId, unitId);
        return new ResponseEntity<>(new ApiResponse<>("success",unitTenants), HttpStatus.OK);

    }

    //search for specific criteria when querying for a unit
    @GetMapping(value = "number-of/rooms")
    public ResponseEntity<ApiResponse<Object>> fetchUnitsByRooms(
            @RequestParam("property_id")Long id,
            @RequestParam("number_of_rooms")Integer noOfRooms
            ){
        List<Units> retrieveUnits = unitService.fetchByNoOfRooms(id,noOfRooms);
        return new ResponseEntity<>(new ApiResponse<>("success", retrieveUnits), HttpStatus.OK);

    }


    @PostMapping(path = "property/{id}/create-unit")
    public ResponseEntity<ApiResponse<Object>> createUnit(
            @PathVariable Long id,
            @RequestBody UnitDto unitDto){
        Long unitId = unitService.createUnit(id, unitDto);
        return new ResponseEntity<>(new ApiResponse<>("success", unitId), HttpStatus.CREATED);
    }


    @PutMapping(value = "update/rent/{id}/{id2}/{price}")
    public ResponseEntity<ApiResponse<?>>updateRentPrice(
            @PathVariable("id")Long propertyId,
            @PathVariable("id2") Long unitId,
            @PathVariable Double price
            ){

        Units updatedRent = unitService.updateRentPrice(propertyId, unitId, price);
        return ResponseEntity.ok(new ApiResponse<>("success", updatedRent));
    }

    //updating the status of a unit (VACANT, OCCUPIED etc)
    @PutMapping(path = "update/status" )
    public ResponseEntity<ApiResponse<Units>> updateUnitStatus(
            @RequestParam("id") Long propertyId,
            @RequestParam("id2") Long unitId,
            @RequestParam ("status") unitStatus status
    ){

        Units updatedUnit = unitService.updateStatus(propertyId, unitId, status);
        return new ResponseEntity<>(new ApiResponse<>("success", updatedUnit),
                HttpStatus.OK);
    }






}
