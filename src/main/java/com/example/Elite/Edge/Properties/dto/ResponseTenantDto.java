package com.example.Elite.Edge.Properties.dto;



import com.example.Elite.Edge.Properties.constants.Status;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
public class ResponseTenantDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
    private Status tenantStatus;

    public ResponseTenantDto(String firstName, String lastName, String email, String phone,
                             Status tenantStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.tenantStatus = tenantStatus;
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



    public Status getTenantStatus() {
        return tenantStatus;
    }
}
