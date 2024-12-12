package com.example.Elite.Edge.Properties.dto;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.model.Lease;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LeaseRequestDto {


    @NonNull
    private LocalDate endDate;

    @NonNull
    private Status status;

    @NonNull
    private String agreementDocument;

    @NonNull
    private Double rentAmount;

    @NonNull
    private Double depositAmount;

    @NonNull
    private Integer terminationNoticePeriod;






}
