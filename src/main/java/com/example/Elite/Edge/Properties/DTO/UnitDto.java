package com.example.Elite.Edge.Properties.DTO;

import com.example.Elite.Edge.Properties.Enums.unitStatus;
import com.example.Elite.Edge.Properties.Enums.unitType;
import com.example.Elite.Edge.Properties.Model.Units;
import org.hibernate.annotations.processing.Pattern;

public class UnitDto {

    private String unitNumber;

    private String FloorNumber;

    private double rentprice;

    private double unitvalue;

    private unitType UnitType;

    private unitStatus Unitstatus;

    private Integer numberofrooms;

    private double deposit;

    public UnitDto(String unitNumber, String floorNumber, double rentprice, double unitvalue,
                   Integer numberofrooms, double deposit) {
        this.unitNumber = unitNumber;
        FloorNumber = floorNumber;
        this.rentprice = rentprice;
        this.unitvalue = unitvalue;
        this.numberofrooms = numberofrooms;
        this.deposit = deposit;
    }

    public UnitDto(Units unit) {
        this.unitNumber = unit.getUnitNumber();
        this.FloorNumber = unit.getFloorNumber();
        this.rentprice = unit.getRentprice();
        this.unitvalue = unit.getUnitvalue();
        this.UnitType = unit.getUnitType();
        this.Unitstatus = unit.getUnitStatus();
        this.numberofrooms = unit.getNumberofrooms();
        this.deposit =  unit.getDeposit();
    }

    public String getUnitNumber() {
        return unitNumber;
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


    public Integer getNumberofrooms() {
        return numberofrooms;
    }



    public double getDeposit() {
        return deposit;
    }
}
