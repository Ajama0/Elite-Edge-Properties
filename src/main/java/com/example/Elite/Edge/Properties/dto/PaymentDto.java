package com.example.Elite.Edge.Properties.dto;

import com.example.Elite.Edge.Properties.constants.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentDto {
    private String reference;
    private PaymentStatus status;
    private Double amount;
    private LocalDate date;



}
