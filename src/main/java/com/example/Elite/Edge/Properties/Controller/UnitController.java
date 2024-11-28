package com.example.Elite.Edge.Properties.Controller;

import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Service.UnitService;
import com.example.Elite.Edge.Properties.Wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            throw new IllegalStateException("properties could not be found");
        }
    }
}
