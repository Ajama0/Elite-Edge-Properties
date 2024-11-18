package com.example.Elite.Edge.Properties.Model;


import com.example.Elite.Edge.Properties.Enums.Type;
import jakarta.persistence.*;

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
    private Type type;

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

    //accessing the units associated with properties
    @OneToMany(mappedBy = "property")
    private List<Units> units;

    @ManyToMany(mappedBy = "properties")
    private List<PropertyOwner> propertyOwners;


    public Property() {
    }

    public Property(String address, Type type, String propertyname, double propertyvalue,
                    Boolean parkingAvailable, String city, String state,
                    String zipcode, LocalDate purchaseDate,
                    Integer rating, String propertydescription) {
        this.address = address;
        this.type = type;
        this.propertyname = propertyname;
        this.propertyvalue = propertyvalue;
        this.parkingAvailable = parkingAvailable;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.purchaseDate = purchaseDate;
        this.rating = rating;
        this.propertydescription = propertydescription;
    }

    public Long getId() {
        return Id;
    }

    public String getAddress() {
        return address;
    }

    public Type getType() {
        return type;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(Type type) {
        this.type = type;
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
                ", type=" + type +
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
