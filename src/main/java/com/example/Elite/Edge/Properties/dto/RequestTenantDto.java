package com.example.Elite.Edge.Properties.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestTenantDto {





    private String firstName;


    private String lastName;


    private String email;


    private String phone;

    private LocalDate dob;


    private String address;


    private String occupation;


    private Double income;


    public RequestTenantDto(String firstName, String lastName, String email ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }



}
