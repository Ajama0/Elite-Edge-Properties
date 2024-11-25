package com.example.Elite.Edge.Properties.Service;


import com.example.Elite.Edge.Properties.Enums.PropertyType;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import com.example.Elite.Edge.Properties.Repository.propertyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        new Property().setAccessedTimeStamp(LocalDate.now());
        return getProperties;
    }

    public Property getPropertyById(Long id) {
       Property propertyById = PropertyRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException(id + "does not exist"));

       propertyById.setAccessedTimeStamp(LocalDate.now());
        PropertyRepository.save(propertyById);

       return propertyById;

    }


    public List<Property> fetchByName(String name) {
        List<Property> propertyname = PropertyRepository.findAll().stream()
                .filter(property -> property.getPropertyname().equals(name))
                .collect(Collectors.toList());

        if (propertyname.isEmpty()){
            throw new EntityNotFoundException("The property" + name + "does not exist!");
        }

        return propertyname;

    }




    public List<Property> fetchRatingAndType(Integer rating, PropertyType type) {
        List<Property> retrieveAvailableTypes = PropertyRepository.findAll()
                .stream()
                .filter(property -> property.getRating().equals(rating) &&
                        property.getType().equals(type) )
                .collect(Collectors.toList());

        if(retrieveAvailableTypes.isEmpty()){
            throw new IllegalStateException("There are no matches for your record");
        }
        Property property = new Property();
        property.setAccessedTimeStamp(LocalDate.now());
        PropertyRepository.save(property);

        return retrieveAvailableTypes;
    }

    public List<PropertyOwner> fetchOwners(Long id) {
        //get the property object associated with the current id.
        //then access the owners associated that reference that property
        //orElseThrow unwraps the optional from findbyid if a object exists
        //hence no need to re-wrap it with optional

        Property property = PropertyRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Property doesnt exist"));

        return property.getPropertyOwners();

    }
}
