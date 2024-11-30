package com.example.Elite.Edge.Properties.DTO;

import com.example.Elite.Edge.Properties.Enums.unitType;

public class UnitDto {

    private String unitNumber;

    private String FloorNumber;

    private double rentprice;

    private double unitvalue;

    private unitType UnitType;

    private Integer numberofrooms;

    private double deposit;

    public UnitDto(String unitNumber, String floorNumber, double rentprice, double unitvalue,
                   unitType UnitType, Integer numberofrooms, double deposit) {
        this.unitNumber = unitNumber;
        FloorNumber = floorNumber;
        this.rentprice = rentprice;
        this.unitvalue = unitvalue;
        this.UnitType = UnitType;
        this.numberofrooms = numberofrooms;
        this.deposit = deposit;
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

    public unitType getUnitType() {
        return UnitType;
    }

    public Integer getNumberofrooms() {
        return numberofrooms;
    }



    public double getDeposit() {
        return deposit;
    }
}
