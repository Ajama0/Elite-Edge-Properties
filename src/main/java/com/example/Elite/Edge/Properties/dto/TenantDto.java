package com.example.Elite.Edge.Properties.dto;



import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.model.Tenants;

import java.time.LocalDate;

public class TenantDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate dob;

    private String address;

    private String occupation;

    private Double income;

    private Status tenantStatus;

    public TenantDto(Tenants tenants) {
        this.firstName = tenants.getFirstName();
        this.lastName = tenants.getLastName();
        this.email = tenants.getEmail();
        this.phone = tenants.getPhone();
        this.dob = tenants.getDob();
        this.address = tenants.getAddress();
        this.occupation = tenants.getOccupation();
        this.income = tenants.getIncome();
        this.tenantStatus = tenants.getTenantStatus();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getOccupation() {
        return occupation;
    }

    public Double getIncome() {
        return income;
    }

    public Status getTenantStatus() {
        return tenantStatus;
    }
}
