package com.example.Elite.Edge.Properties.Controller;


import com.example.Elite.Edge.Properties.DTO.PropertyDTO;
import com.example.Elite.Edge.Properties.Exceptions.PropertyException;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import com.example.Elite.Edge.Properties.Service.propertyService;
import com.example.Elite.Edge.Properties.Wrapper.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ApiResponse<PropertyDTO>>createProperty(@RequestBody PropertyDTO propertyDTO){
       try {
          PropertyDTO property = propertyservice.CreateProperty(propertyDTO);
           return ResponseEntity.ok(new ApiResponse<>("successfully created!",
                   property
           ));
       }catch (RuntimeException runtimeException){
           throw new PropertyException("Property could not be created at this time");
       }


    }

    //update price, update parking available,
    //this would later be strictly allowed for Admins (Spring security)
    @PutMapping(value = "/update/{id}/{price}")
    public ResponseEntity<ApiResponse<Object>>UpdatePrice(@PathVariable Long id,
            @PathVariable("price") double PropertyPrice){
        try{
            PropertyDTO updatedProperty = propertyservice.PropertyPrice(id,
                    PropertyPrice);

            return ResponseEntity.ok(new ApiResponse<>("success" , updatedProperty));
        }catch(RuntimeException runtimeException){
            throw new PropertyException("Property price could not be updated");
        }
    }


    //including description in the @PathVariable could affect url limit
    //assign it to a request body map and retrieve the value of key as our description
    //now you can handle the client request.
    @PutMapping(value = "update-propertyDescription/{id}")
    public ResponseEntity<ApiResponse<Object>>updateDescription(@PathVariable Long id,
                                      @RequestBody Map<String, String> payload){

        String description = payload.get("description");
        try{
            PropertyDTO propertyDTO = propertyservice.updateDescription(id, description);
            return ResponseEntity.ok(new ApiResponse<>("Successfully updated",
                    propertyDTO
                    ));
        }catch (RuntimeException runtimeException){
            throw new PropertyException("Property with id: " + id + " could not be updated");
        }

    }


    @DeleteMapping(value = "unlisted-property/{id}")
    public ResponseEntity<ApiResponse<Void>>deleteProperty(@PathVariable Long id){

        propertyservice.deleteProperty(id);
        return ResponseEntity.ok(new ApiResponse<>("successfully deleted",null));





    }














}
