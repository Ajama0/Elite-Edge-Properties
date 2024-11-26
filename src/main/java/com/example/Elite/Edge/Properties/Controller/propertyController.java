package com.example.Elite.Edge.Properties.Controller;


import com.example.Elite.Edge.Properties.DTO.PropertyDTO;
import com.example.Elite.Edge.Properties.DTO.PropertyOnwerDto;
import com.example.Elite.Edge.Properties.Exceptions.PropertyException;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import com.example.Elite.Edge.Properties.Service.propertyService;
import com.example.Elite.Edge.Properties.Wrapper.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Properties")
public class propertyController {

    //TODO add new custom exception handler to http methods
    private final propertyService propertyservice;

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
    public ResponseEntity<?> fetchPropertyById(@PathVariable("id") Long id ){
        Property propertyById = propertyservice.getPropertyById(id);
        return ResponseEntity.ok(new ApiResponse<>("success",
                propertyById));
    }

    @GetMapping(path = "retrieve/property/name")
    public ResponseEntity<?>fetchPropertyname(@RequestParam String name){
        List<Property> propertyname = propertyservice.fetchByName(name);
        return ResponseEntity.ok(new ApiResponse<>("success", propertyname));
    }

    // TODO - double check logic again
    @GetMapping(path = "retrieve/Apartment/Availability/E/Type")
    public ResponseEntity<?> findRatingAndType(@RequestParam Integer rating,
                                                         @RequestParam String type){
        try {
            List<Property> getAvailableTypes = propertyservice.fetchRatingAndType(
                    rating, Property.serializeEnum(type));
            return ResponseEntity.ok(new ApiResponse<>("success", getAvailableTypes));

        }catch (RuntimeException e){
            throw new PropertyException("ensure rating are type are selected properly");
        }
    }

    @GetMapping(path = "property/{id}/owners")
    public  ResponseEntity<Object> retrievePropertyOwners(@PathVariable("id") Long id){
        try{
            List< PropertyOwner> propertyOwners = propertyservice.fetchOwners(id);
            return ResponseEntity.ok( new ApiResponse<>("success",
                    propertyOwners));
        }catch (RuntimeException e){
            throw new PropertyException("property owners for the id do not exist");
        }
    }
    //allow buying a property -> adding to property owner
    //creating a property
    @PostMapping(path = "create-property")
    public ResponseEntity<ApiResponse>createProperty(@RequestBody PropertyDTO propertyDTO){
       try {
          PropertyDTO property = propertyservice.CreateProperty(propertyDTO);
           return ResponseEntity.ok(new ApiResponse<>("successfully created!",
                   property
           ));
       }catch (RuntimeException runtimeException){
           throw new PropertyException("Property could not be created at this time");
       }
    }












}
