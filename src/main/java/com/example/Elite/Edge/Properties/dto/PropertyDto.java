package com.example.Elite.Edge.Properties.dto;

import com.example.Elite.Edge.Properties.constants.PropertyType;
import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.model.Property;

import java.time.LocalDate;

public class PropertyDto {
    //hide internals of the property entity from client
    private String address;
    private PropertyType propertyType;
    private String propertyName;
    private double propertyValue;
    private Boolean parkingAvailable;
    private String city;
    private String state;
    private String zipcode;
    private LocalDate purchaseDate;
    private Integer rating;
    private String propertyDescription;

    private  Status status;

    public PropertyDto(String address, String propertyName, PropertyType propertyType, double propertyValue, Boolean parkingAvailable, String city, String state,
                       String zipcode, LocalDate purchaseDate, Integer rating,
                       String propertyDescription, Status status) {
        this.address = address;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.parkingAvailable = parkingAvailable;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.purchaseDate = purchaseDate;
        this.rating = rating;
        this.propertyDescription = propertyDescription;
        this.status = status;
    }

    public PropertyDto(Property property){
        this.address = property.getAddress();
        this.propertyName = property.getPropertyname();
        this.propertyType = property.getPropertyType();
        this.propertyValue = property.getPropertyvalue();
        this.parkingAvailable = property.getParkingAvailable();
        this.city = property.getCity();
        this.state = property.getState();
        this.zipcode = property.getZipcode();
        this.purchaseDate = property.getPurchaseDate();
        this.rating = property.getRating();
        this.propertyDescription = property.getPropertydescription();
        this.status = property.getStatus();
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public Boolean getParkingAvailable() {
        return parkingAvailable;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Integer getRating() {
        return rating;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPropertyValue(double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setParkingAvailable(Boolean parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }
}
