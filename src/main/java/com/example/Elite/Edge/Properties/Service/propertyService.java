package com.example.Elite.Edge.Properties.Service;


import com.example.Elite.Edge.Properties.DTO.PropertyDTO;
import com.example.Elite.Edge.Properties.DTO.PropertyOnwerDto;
import com.example.Elite.Edge.Properties.Enums.PropertyType;
import com.example.Elite.Edge.Properties.Exceptions.PropertyException;
import com.example.Elite.Edge.Properties.Model.Property;
import com.example.Elite.Edge.Properties.Model.PropertyOwner;
import com.example.Elite.Edge.Properties.Model.Units;
import com.example.Elite.Edge.Properties.Repository.propertyRepository;
import com.example.Elite.Edge.Properties.Repository.unitRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class propertyService {

    private final propertyRepository  PropertyRepository;
    private final unitRepository unitrepository;

    @Autowired
    public propertyService(propertyRepository  PropertyRepository, unitRepository unitrepository){
        this.PropertyRepository = PropertyRepository;
        this.unitrepository = unitrepository;
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

    @Transactional
    public PropertyDTO CreateProperty(PropertyDTO propertyDTO) {
    //let's ensure the property doesn't already exist in our system
       Optional<Property>CheckDuplicate = PropertyRepository.findByPropertyNameAndAddress(propertyDTO.getPropertyName(),
               propertyDTO.getAddress());

       if(CheckDuplicate.isPresent()){
           throw new PropertyException("Record already exists!");
       }

       Property property = new Property(
            propertyDTO.getAddress(),
            propertyDTO.getPropertyType(),
            propertyDTO.getPropertyName(),
            propertyDTO.getPropertyValue(),
            propertyDTO.getParkingAvailable(),
            propertyDTO.getZipcode(),
            propertyDTO.getState(),
            propertyDTO.getCity(),
            propertyDTO.getPurchaseDate(),
            propertyDTO.getRating(),
            propertyDTO.getPropertyDescription());

            Property savedProperty = PropertyRepository.save(property);

            //Map the property back to a DTO to return to the client

           return new PropertyDTO(savedProperty);

    }

    public PropertyDTO PropertyPrice(Long id, double propertyValue) {
        Property property = PropertyRepository.findById(id)
                .orElseThrow(() -> new PropertyException("property does not exist"));

            //ensure the same price is not being entered
            if(property.getPropertyvalue() == propertyValue){
                throw new PropertyException("The value of the property has already been set");
            }

            property.setPropertyvalue(propertyValue);
            Property property1 =  PropertyRepository.save(property);

            return new PropertyDTO(property1);
    }

    @Transactional
    public PropertyDTO updateDescription(Long id, String description) {
        Property propertyId = PropertyRepository.findById(id)
                .orElseThrow(()-> new PropertyException("id does not exist"));

        if(propertyId.getPropertydescription().equals(description)){
            throw new PropertyException("The description must be different to the previous one entered");
        }

        propertyId.setPropertydescription(description);

        Property property  = PropertyRepository.save(propertyId);

        return new PropertyDTO(property);

    }

    //assume a property has been sold
    @Transactional
    public void DeleteProperty(Long id) {
        Optional<Property> deletePropertyId = PropertyRepository.findById(id);

        if(deletePropertyId.isEmpty()){
            throw new PropertyException(id + " does not exist");
        }

        Property property = deletePropertyId.get();
        //deleting the associated units.
        //deleting the associated property owners
        //other owners could be associated to other properties
        //hence we remove the association between owners and the property

        //for each property we get their owner, and remove the association
        property.getPropertyOwners().forEach(propertyOwner -> propertyOwner.getProperties()
                .remove(property));

        property.getPropertyOwners().clear();



        List<Units> associatedUnits = property.getUnits();
        unitrepository.deleteAll(associatedUnits);
        PropertyRepository.delete(property);





    }
}
