package com.example.Elite.Edge.Properties.mapper;

import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.dto.RequestTenantDto;
import com.example.Elite.Edge.Properties.dto.ResponseTenantDto;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;

public class TenantMapper {


    /*
    we will convert TenantDTO->Tenant when receiving a request-body for Tenant creation,
    when creating a tenant we don't want to expose our entire entity as there is other relations

    vice versa
    when returning tenants, we don't want to expose internals, hence we pass back a responseDTO.
     */


    //allows us to call directly from class name
    public static ResponseTenantDto mapTenantsForResponse(Tenants tenants){
        return new ResponseTenantDto(
                tenants.getFirstName(),
                tenants.getLastName(),
                tenants.getEmail(),
                tenants.getPhone(),
                tenants.getTenantStatus()
        );

    }


    //the request body given to the client when creating a tenant is RequestTenantDto
    //we convert the dto to a tenant entity to save
    //status is automatically set as active in the server
    public static Tenants mapRequestToTenants(RequestTenantDto requestTenantDto){
        return new Tenants(
                requestTenantDto.getFirstName(),
                requestTenantDto.getLastName(),
                requestTenantDto.getEmail(),
                requestTenantDto.getPhone(),
                requestTenantDto.getDob(),
                requestTenantDto.getAddress(),
                requestTenantDto.getOccupation(),
                requestTenantDto.getIncome(),
                Status.ACTIVE
        );
    }
}
