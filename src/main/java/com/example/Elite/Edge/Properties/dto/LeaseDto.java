package com.example.Elite.Edge.Properties.dto;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.model.Lease;
import com.example.Elite.Edge.Properties.model.Tenants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Getter
public class LeaseDto {

    //only return the id, status and agreement of the lease


    private Long id;
    private Status status;
    private String AgreementPdf;


    private LeaseDto(Lease lease){
        this.id = lease.getId();
        this.status = lease.getStatus();
        this.AgreementPdf = lease.getAgreementDocument();
    }


}
