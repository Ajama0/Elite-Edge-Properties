package com.example.Elite.Edge.Properties.Service;


import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Repository.propertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class propertyService {

    private propertyRepository  PropertyRepository;

    @Autowired
    public propertyService(propertyRepository  PropertyRepository){
        this.PropertyRepository = PropertyRepository;
    }



    public List<Property> getAllProperties(){
        List<Property> getProperties = PropertyRepository.findAll();

        if(getProperties.isEmpty()){
            throw new IllegalStateException("There are Currently no properties.. sorry!");


        }
        return getProperties;
    }
}
