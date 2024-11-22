package com.example.Elite.Edge.Properties.Model;


import com.example.Elite.Edge.Properties.Enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Property")
public class Property {



    @Id
    @SequenceGenerator(name = "property_sequence",
            sequenceName = "property_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "property_sequence"

    )
    private Long Id;

    @Column(nullable = false, unique = true)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PropertyType propertyType;

    @Column(name = "property_name", nullable = false)
    private String propertyname;

    @Column(name = "property_value")
    private double propertyvalue;

    @Column(name = "parking_available", nullable = false)
    private Boolean parkingAvailable;

    private String city;

    private String state;

    @Column(name = "zip_code")
    private String zipcode;


    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    private Integer rating; //default value Null in case rating is not provided yet

    @Column(name = "property_description")
    private String propertydescription;

    @Column(name = "accessed_timestamp")
    private LocalDate accessedTimeStamp;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column(name = "updated_at")
    private LocalDate updated_at;

    //accessing the units associated with properties
    @OneToMany(mappedBy = "property")
    private List<Units> units;


    //add fetch type as lazy to ensure we don't return associated owners unless explicitly done
    @ManyToMany(mappedBy = "properties", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PropertyOwner> propertyOwners;


    public Property() {
    }

    public Property(String address, PropertyType propertyType, String propertyname, double propertyvalue,
                    Boolean parkingAvailable, String city, String state,
                    String zipcode, LocalDate purchaseDate,
                    Integer rating, String propertydescription
                    ) {
        this.address = address;
        this.propertyType = propertyType;
        this.propertyname = propertyname;
        this.propertyvalue = propertyvalue;
        this.parkingAvailable = parkingAvailable;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.purchaseDate = purchaseDate;
        this.rating = rating;
        this.propertydescription = propertydescription;
        this.accessedTimeStamp = LocalDate.now();
        this.created_at = LocalDate.now();
        this.updated_at = LocalDate.now();
    }


    public List<PropertyOwner> getPropertyOwners(){
        return propertyOwners;
    }

    public void setPropertyOwners(List<PropertyOwner> propertyOwners){
        this.propertyOwners = propertyOwners;
    }
    public Long getId() {
        return Id;
    }
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public LocalDate getAccessedTimeStamp() {
        return accessedTimeStamp;
    }

    public List<Units> getUnits() {
        return units;
    }

    public LocalDate getCreated_at(){
        return created_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public String getAddress() {
        return address;
    }

    public PropertyType getType() {
        return propertyType;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public double getPropertyvalue() {
        return propertyvalue;
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

    public String getPropertydescription() {
        return propertydescription;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setAccessedTimeStamp(LocalDate accessedTimeStamp) {
        this.accessedTimeStamp = accessedTimeStamp;
    }

    public void setUnits(List<Units> units) {
        this.units = units;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    public void setPropertyvalue(double propertyvalue) {
        this.propertyvalue = propertyvalue;
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

    public void setPropertydescription(String propertydescription) {
        this.propertydescription = propertydescription;
    }

    @Override
    public String toString() {
        return "Property{" +
                "Id=" + Id +
                ", address='" + address + '\'' +
                ", type=" + propertyType +
                ", propertyname='" + propertyname + '\'' +
                ", propertyvalue=" + propertyvalue +
                ", parkingAvailable=" + parkingAvailable +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", rating=" + rating +
                ", propertydescription='" + propertydescription + '\'' +
                '}';
    }
}
