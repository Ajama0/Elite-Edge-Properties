package com.example.Elite.Edge.Properties.Model;


import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Enums.unitType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Units {

    public Units(String unitNumber, unitStatus unitStatus, String floorNumber,
                 double rentprice, double unitvalue, unitType unitType, Integer numberofrooms,
                 double deposit ){
        this.unitNumber = unitNumber;
        this.unitStatus = unitStatus;
        FloorNumber = floorNumber;
        this.rentprice = rentprice;
        this.unitvalue = unitvalue;
        this.unitType = unitType;
        this.numberofrooms = numberofrooms;
        this.deposit = deposit;

    }

    @Id
    @SequenceGenerator(name = "units_sequence",
            sequenceName = "units_seq",
            allocationSize = 1

    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "units_sequence"

    )
    private Long id;

    @Column(name = "unit_number", nullable = false)
    private String unitNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_status", nullable = false)
    private unitStatus unitStatus;

    @Column(name = "Floor_number", nullable = false)
    private String FloorNumber;

    @Column(name = "rent_price")
    private double rentprice;

    @Column(name = "unit_value")
    private double unitvalue;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false)
    private unitType unitType; //whether it is an apartment, an office, studio etc

    @Column(name = "number_of_rooms")
    private Integer numberofrooms;

    @Column(name = "deposit_amount")
    private double deposit;


    /**
     * Entity relationships
     * property->units 1:M  -- bi-directional
     * landlord -> units 1:M  bi-directional
     * units -> Tenants 1:1
     * units->lease 1:M bi-directional, non-owning side is units as it doesnt need to hold store
     * informatoin about a lease
     */

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;



    @OneToOne(mappedBy = "units")
    private Tenants tenant;

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY )
    @JsonIgnore
    private List<Lease> lease = new ArrayList<>();


    public String getUnitNumber() {
        return unitNumber;
    }

    public unitStatus getUnitStatus() {
        return unitStatus;
    }

    public String getFloorNumber() {
        return FloorNumber;
    }

    public double getRentprice() {
        return rentprice;
    }

    public double getUnitvalue() {
        return unitvalue;
    }

    public unitType getUnitType() {
        return unitType;
    }

    public Integer getNumberofrooms() {
        return numberofrooms;
    }

    public double getDeposit() {
        return deposit;
    }

    public Long getId() {
        return id;
    }

    public Property getProperty() {
        return property;
    }

    public Tenants getTenant() {
        return tenant;
    }

    public List<Lease> getLease() {
        return lease;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public void setUnitStatus(com.example.Elite.Edge.Properties.Enums.unitStatus unitStatus) {
        this.unitStatus = unitStatus;
    }

    public void setFloorNumber(String floorNumber) {
        FloorNumber = floorNumber;
    }

    public void setRentprice(double rentprice) {
        this.rentprice = rentprice;
    }

    public void setUnitvalue(double unitvalue) {
        this.unitvalue = unitvalue;
    }

    public void setUnitType(com.example.Elite.Edge.Properties.Enums.unitType unitType) {
        this.unitType = unitType;
    }

    public void setNumberofrooms(Integer numberofrooms) {
        this.numberofrooms = numberofrooms;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public void setProperty(Property property) {
        this.property = property;
    }



    public void setTenant(Tenants tenant) {
        this.tenant = tenant;
    }

    public void setLease(List<Lease> lease) {
        this.lease = lease;
    }

    @Override
    public String toString() {
        return "Units{" +
                "id=" + id +
                ", unitNumber='" + unitNumber + '\'' +
                ", unitStatus=" + unitStatus +
                ", FloorNumber='" + FloorNumber + '\'' +
                ", rentprice=" + rentprice +
                ", unitvalue=" + unitvalue +
                ", unitType=" + unitType +
                ", numberofrooms=" + numberofrooms +
                ", deposit=" + deposit +
                ", property=" + property +
                ", tenant=" + tenant +
                ", lease=" + lease +
                '}';
    }
}
