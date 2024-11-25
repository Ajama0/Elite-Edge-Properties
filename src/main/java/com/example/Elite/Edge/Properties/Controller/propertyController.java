package com.example.Elite.Edge.Properties.Controller;


import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Service.propertyService;
import com.example.Elite.Edge.Properties.apiWrapper.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/Properties")
public class propertyController {

    private propertyService propertyservice;

    @Autowired
    public propertyController(propertyService propertyService){
        this.propertyservice = propertyService;
    }



    @GetMapping(path = "/properties")
    public ResponseEntity<?> fetchAllProperties(){
        List<Property> getproperties = propertyservice.getAllProperties();
        return ResponseEntity.ok(new ApiResponse<>("success", getproperties));

        }


    @GetMapping(path = "properties/{id}")
    public ResponseEntity<?> fetchPropertyById(@PathVariable Long id ){
        Property propertyById = propertyservice.getPropertyById(id);
        return ResponseEntity.ok(new ApiResponse<>("success",
                propertyById));
    }

    @GetMapping(path = "retrieve/property/name")
    public ResponseEntity<?>fetchPropertyname(@RequestParam String name){
        List<Property> propertyname = propertyservice.fetchByName(name);
        return ResponseEntity.ok(new ApiResponse<>("success", propertyname));
    }

    @GetMapping(path = "retrieve/Aparment/Availablity/E/Type")
    public ResponseEntity<?> findRatingAndType(@RequestParam Integer rating,
                                                         @RequestParam String type){
        try {
            List<Property> getAvailableTypes = propertyservice.fetchRatingAndType(
                    rating, type);
            return ResponseEntity.ok(new ApiResponse<>("success", getAvailableTypes));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }





    }












}
