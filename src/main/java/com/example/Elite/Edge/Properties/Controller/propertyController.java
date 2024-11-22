package com.example.Elite.Edge.Properties.Controller;


import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Service.propertyService;
import com.example.Elite.Edge.Properties.apiWrapper.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/Properties")
public class propertyController {

    private propertyService propertyservice;

    @Autowired
    public propertyController(propertyService propertyService){
        this.propertyservice = propertyService;
    }



    @GetMapping(path = "/getProperties")
        public ResponseEntity<ApiResponse> getAllProperties(){
           List<Property> getproperties = propertyservice.getAllProperties();
           return ResponseEntity.ok(new ApiResponse<>("success", getproperties));

        }





}
