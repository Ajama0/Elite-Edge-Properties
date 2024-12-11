package com.example.Elite.Edge.Properties.controller;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.dto.RequestTenantDto;
import com.example.Elite.Edge.Properties.dto.ResponseTenantDto;
import com.example.Elite.Edge.Properties.model.Tenants;
import com.example.Elite.Edge.Properties.model.Units;
import com.example.Elite.Edge.Properties.service.TenantsService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import io.micrometer.common.lang.Nullable;
import org.apache.coyote.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/tenants")
public class TenantsController {

    private final TenantsService tenantsService;


    public TenantsController(TenantsService tenantsService){
        this.tenantsService = tenantsService;
    }


    //hide internals with DTO when retrieving tenants
    //when fetching tenants, we can filter them by property but also include their unit

    @GetMapping("All")
    public ResponseEntity<ApiResponse<List<ResponseTenantDto>>> fetchTenants(
            @RequestParam("property_id") Long propertyId

    ){
        List<ResponseTenantDto> responseTenantDtoList = tenantsService.fetchAllTenants(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("success", responseTenantDtoList));


    }

    //fetch Tenants based off their status(Active, Deleted) etc


    @GetMapping("by/status")
    public ResponseEntity<ApiResponse<Object>>fetchByStatus(
            @RequestParam("property_id") Long propertyId,
            @RequestParam("status")Status status
            ){
        List<ResponseTenantDto> Tenants= tenantsService.retrieveByStatus(propertyId, status);
        return new ResponseEntity<>(new ApiResponse<>("Success",Tenants ),
                HttpStatus.OK);

    }

    @GetMapping(path = "retrieve/by/occupation")
    public ResponseEntity<ApiResponse<Object>> fetchByOccupation(
            @RequestParam("property_id") Long propertyId,
            @RequestParam("Occupation") String Occupation
    ){
        List<ResponseTenantDto> tenants = tenantsService.findByOccupation(propertyId, Occupation);
        return new ResponseEntity<>(new ApiResponse<>("success", tenants),
                HttpStatus.OK);


    }



    @GetMapping(path = "{id}/unit")
    public ResponseEntity<ApiResponse<Units>> findTenantApartment(
            @PathVariable("id") Long tenantId
    ){
        Units unit = tenantsService.findTenantApt(tenantId);
        return ResponseEntity.ok(new ApiResponse<>("success", unit));
    }


    @GetMapping(path = "")



    //Best practice is to typically return the id of a POST request
    @PostMapping(path = "create")
    public ResponseEntity<ApiResponse<Long>> createTenant(
            @RequestBody RequestTenantDto requestTenantDto,
            @RequestParam("unit") Long unitId
    ){
        Long tenantId = tenantsService.createTenant(requestTenantDto, unitId);
        return new ResponseEntity<>(new ApiResponse<>("success", tenantId),
                HttpStatus.CREATED);
    }


    /**
     * updateEmail: allow the tenant user to update their email
     * @param requestTenantDto: custom dto to hide internals
     *  @param email : new email to set
     * include ResponseTenantDTO to return obj only showing fields wanted to be returned
     * @return custom api wrapper: return api wrapper with 200 response
     */

    @PutMapping(path = "update/email/{email}")
    public ResponseEntity<ApiResponse<ResponseTenantDto>> updateTenantEmail(
            @PathVariable("email") String email,
            @RequestBody RequestTenantDto requestTenantDto

    ) {

        ResponseTenantDto updatedTenant = tenantsService.updateEmail(email, requestTenantDto);
        return new ResponseEntity<>(new ApiResponse<>("success", updatedTenant),
                HttpStatus.OK);


    }


    /**
     * updateTenantIncome: allow the tenant user to update their email
     * @param requestTenantDto: custom dto to hide internals
     *  @param income : new income to set
     * include ResponseTenantDTO to return obj only showing fields id and income
     * @return custom api wrapper: return api wrapper with 200 response
     */

    @PutMapping(path = "update/income")
    public ResponseEntity<ApiResponse<?>> updateTenantIncome(
            @RequestBody RequestTenantDto requestTenantDto,
            @RequestParam ("income") double income
    ){
        ResponseTenantDto dto = tenantsService.updateIncome(requestTenantDto, income);
        return new ResponseEntity<>(new ApiResponse<>("success",dto),
             HttpStatus.OK);
    }

    /**
     * updateTenantOccupation - allows the tenant to update their occupation/job
     * @param tenantId: refers to the tenants id
     * @param Occupation: the new occupation they wish to replace their previous one with
     * return ResponseTenantDTO: return dto including only relevant info and hiding internals
     */
    @PutMapping(path = "update/occupation/{id}/{occupation}")
    public ResponseEntity<ApiResponse<ResponseTenantDto>> updateTenantOccupation(
            @PathVariable("id") Long tenantId,
            @PathVariable("occupation") String Occupation
    ){
        ResponseTenantDto tenantDto = tenantsService.updateOccupation(tenantId, Occupation);
        return new ResponseEntity<>(new ApiResponse<>("success", tenantDto),
                HttpStatus.OK);
    }


    /**
     * required : Boolean param which checks if tenants will return another unit, if so set their field to archived.
     * archived tenants will be contacted to get an update on if they are still willing to rent a property
     * deleteTenant - Tenants who are no longer rent a unit, will have their field set to deleted
     * ideally this would be a left active if renting another unit in the property, otherwise soft delete
     * we will remove the association between that unit and tenant
     * and any lease associated to the unit with that tenant will be set to archived
     * @param unitId: identifying the tenants unit
     * @param tenantId : the tenants Identifier
     * @param rentAgain: boolean value to see if the user will rent another unit in the property

     */

    @DeleteMapping("delete/unit")
    public ResponseEntity<ApiResponse<?>> deleteTenant(
            @RequestParam(value = "id") Long tenantId,
            @RequestParam(value = "unit") Long unitId,
            @RequestParam(value = "rentAgain", required = false) Boolean rentAgain

    ){
        Long tenant= tenantsService.deleteTenant(tenantId, unitId,rentAgain);
        return new ResponseEntity<>(new ApiResponse<>("successfully deleted", tenant),
                HttpStatus.OK);

    }









}
