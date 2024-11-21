package com.example.Elite.Edge.Properties.Model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "Property_Owner")
public class PropertyOwner{


    @Id
    @SequenceGenerator(name = "owner_sequence",
            sequenceName = "owner_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "owner_sequence"

    )
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String FirstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "dob", nullable = false)
    private LocalDate date;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;




    @ManyToMany
    @JoinTable(name = "property_0wners_m2m",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    private List<Property> properties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }


    public PropertyOwner(String firstName, String lastName, String email, String address,
                         LocalDate date, String phoneNumber) {
        FirstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.date = date;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PropertyOwner{" +
                "id=" + id +
                ", FirstName='" + FirstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", properties=" + properties +
                '}';
    }
}
