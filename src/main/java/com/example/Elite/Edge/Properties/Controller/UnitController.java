package com.example.Elite.Edge.Properties.Controller;

import com.example.Elite.Edge.Properties.Exceptions.UnitException;
import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Service.UnitService;
import com.example.Elite.Edge.Properties.Wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
            return ResponseEntity.ok(new ApiResponse<>("success", retrieveProperty));
        }catch (RuntimeException runtimeException){
            throw new UnitException("units for property: " + propertyId + " were not found");
        }
    }


}
