package com.example.Elite.Edge.Properties.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPaymentDto {
    private Double amount;
    private String cardLastFour;
}
