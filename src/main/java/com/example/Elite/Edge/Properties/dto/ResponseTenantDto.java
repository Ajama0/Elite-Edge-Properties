package com.example.Elite.Edge.Properties.dto;



import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTenantDto {

    private Long Id;
    private String firstName;

    private String lastName;

    private String email;

    private String phone;
    private Status tenantStatus;

    private String Occupation;


    private Double income;





    public ResponseTenantDto(String firstName, String lastName, String email, String phone,
                             Status tenantStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.tenantStatus = tenantStatus;
    }


    //use serializer so we can initialize values but exclude them from our dto


    public ResponseTenantDto(Long id, String firstName, String Occupation){
        this.Id = id;
        this.firstName = firstName;
        this.Occupation = Occupation;
    }

    public ResponseTenantDto(Tenants tenants){
        this.Id = tenants.getId();
        this.income = tenants.getIncome();
    }

  
    public Long getId() {
        return Id;
    }

    public String getOccupation() {
        return Occupation;
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
