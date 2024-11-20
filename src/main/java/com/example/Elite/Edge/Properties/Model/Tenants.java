package com.example.Elite.Edge.Properties.Model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity
public class Tenants {

    @Id
    @SequenceGenerator(name = "tenant_sequence",
            sequenceName = "units_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "tenant_sequence"

    )
    private Long id;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String address;

    @Column
    private String occupation;

    @Column
    private Double income;

    @OneToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Units units;

    @OneToOne(mappedBy = "Tenants")
    private Lease leases;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    private Lease getLeases(){
        return leases;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private void setLeases(Lease leases){
        this.leases = leases;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Units getUnit() {
        return units;
    }

    public void setUnit(Units units) {
        this.units = units;
    }

    public Units getUnits() {
        return units;
    }

    // Optional: Custom Constructor
    public Tenants(String firstName, String lastName, String email, String phone, LocalDate dob,
                   String address, String occupation, Double income, Units units) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.occupation = occupation;
        this.income = income;
        this.units = units;
    }

    @Override
    public String toString() {
        return "Tenants{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", occupation='" + occupation + '\'' +
                ", income=" + income +
                ", unit=" + (units != null ? units.getId() : "null") +
                '}';
    }
}
