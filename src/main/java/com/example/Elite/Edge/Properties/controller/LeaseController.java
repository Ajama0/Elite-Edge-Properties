package com.example.Elite.Edge.Properties.controller;


import com.example.Elite.Edge.Properties.constants.Status;
import com.example.Elite.Edge.Properties.dto.LeaseDto;
import com.example.Elite.Edge.Properties.dto.LeaseRequestDto;
import com.example.Elite.Edge.Properties.dto.PaymentDto;
import com.example.Elite.Edge.Properties.mapper.LeaseMapper;
import com.example.Elite.Edge.Properties.model.Lease;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.service.LeaseService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/lease")
public class LeaseController {


    private final LeaseService leaseService;

    public LeaseController(LeaseService leaseService){
        this.leaseService = leaseService;
    }


    //Add pagination for retrieving leases for a unit

    /**
     * fetchAllLeases: we retrieve all leases associated to a specific unit
     * @param propertyId : defines the property of the unit
     * @param unitId : the unit were going to get the leases for
     * @param pageNo : for pagination, we define what our page number is, default = 0
     * @param pageSize : pagesize determines the number of records on a single page being returned
     * @return ResponseEntity(new APiResponse<>) return our custom Api wrapped with a dto
     */
    @GetMapping("/All")
    public ResponseEntity<ApiResponse<?>> fetchAllLeases(
            @RequestParam(name = "property_id") Long propertyId,
            @RequestParam(name = "unit_id") Long unitId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        LeaseMapper leases = leaseService.fetchAllLeases(propertyId, unitId, pageNo, pageSize);
        return new ResponseEntity<>(new ApiResponse<>("success", leases), HttpStatus.OK);

    }



    /**
     * //assume after endpoint /All we get all the leases associated to a unit, from here we can select a lease id, and find the payment history of that lease
     * @param LeaseId : the lease id we will fetch payments for
     * @return : paymentDto to hide internals
     */
    @GetMapping(value = "payment/history")
    public ResponseEntity<ApiResponse<?>> retrieveLeasePayments(
            @RequestParam("id") Long LeaseId
    ){
        List<PaymentDto> paymDto = leaseService.fetchLeasePayment(LeaseId);
        return new ResponseEntity<>(new ApiResponse<>("success", paymDto),
                HttpStatus.OK);
    }

    /**
     *LeasePerTenantUnit: allows us to return the lease associated to a tenant and a unit
     * @param unitId : The unit id passed to query the lease, if the unitId is not passed we return the lease associated to the tenant assuming tenant was passed
     * @param tenantId : the tenant id being queried, assuming the tenantId was not passed, we return the leases associated to a unit. unit and lease 1:M
     * @return Object : we can either return a list or a single object, hence the usage of Object return type as return type depends on client input
     */

    @GetMapping(value = "by/tenant/unit")
    public ResponseEntity<ApiResponse<?>> LeasePerTenantUnit(
            @RequestParam(value = "unit", required = false) Long unitId,
            @RequestParam(value = "tenant", required = false) Long tenantId
    ){
        Object dto = leaseService.fetchTenantLeaseByUnit(
                unitId, tenantId
        );
        return new ResponseEntity<>(new ApiResponse<>("success", dto),
                HttpStatus.OK
        );
    }

    /**
     * activeLeasePerProperty: returns all current active leases in a property
     * @param propertyId : represents the property in which we will query to return all the active Leases
     * @return LeaseDto : return a list of lease dtos to hide internals
     */
    @GetMapping(path = "active/{id}")
    public  ResponseEntity<ApiResponse<List<LeaseDto>>> activeLeasePerProperty(
            @PathVariable("id") Long propertyId
    ){
        List<LeaseDto> activeLeases = leaseService.activeLeases(propertyId);
        return new ResponseEntity<>(new ApiResponse<>("success", activeLeases),
                HttpStatus.OK);
    }

    /**
     * LeaseBystatus: fetch the leases of a unit according to the status provided by the client
     * @param propertyId: property id used to query the leases of units associated to a property
     * @param unitId : the unit Id of the current status lease
     * @param status : status enum we use to query what type of lease is to be returned
     * @return ApiResponse wrapped in a 200 status code response
     */
    @GetMapping(path = "/status")
    public ResponseEntity<ApiResponse<?>> leaseByStatus(
            @RequestParam("property") Long propertyId,
            @RequestParam("unit") Long unitId,
            @RequestParam Status status){

        List<LeaseDto> retrieveLeases = leaseService.leasesByStatus(propertyId, unitId, status);
        return ResponseEntity.ok(new ApiResponse<>("success", retrieveLeases));
    }

    /**
     * Expiring Leases, returns all expired leases that expire within 10 days from the current date
     * @param propertyId : the property id used to query all expiring leases within a few days
     * @return
     */
    @GetMapping(path = "/expiring/{property}")
    public ResponseEntity<ApiResponse<List<LeaseDto>>> expiringLeases(
            @PathVariable("property") Long propertyId
    ){
        List<LeaseDto> dueLeases = leaseService.fetchAllExpiringLeases(propertyId);
        return new ResponseEntity<>(new ApiResponse<>("success", dueLeases),
                HttpStatus.OK);
    }

    /**
     *
     * @param propertyId: Property Id to query when creating the lease
     * @param unitId : The unitId refers to the unit in which the lease is for
     * @param tenantId : specific tenant associated with the lease
     * @param leaseDto : LeaseRequestDto to hide internals of lease Entity
     * @return Long: return the Id of the Lease as a Long data type
     */

    @PostMapping(path = "create")
    public ResponseEntity<ApiResponse<Long>> createLease(
            @RequestParam("property") Long propertyId,
            @RequestParam("unit")   Long unitId,
            @RequestParam("tenant") Long tenantId,
            @RequestBody  LeaseRequestDto leaseDto

            ){
        Long leaseId = leaseService.createLease(propertyId, unitId, tenantId, leaseDto);
        return new ResponseEntity<>(new ApiResponse<>("successfully created", leaseId),
                HttpStatus.CREATED);
    }

    /**
     * Update the status of a lease.
     *
     * @param leaseId the unique identifier of the lease to update
     * @param status the new status to assign to the lease (e.g., ACTIVE, TERMINATED)
     * @return ResponseEntity containing an ApiResponse with the updated LeaseDto and HTTP status
     */
    @PutMapping(path = "/change/status/{status}/{id}")
    public ResponseEntity<ApiResponse<LeaseDto>> updateStatus(
            @PathVariable("id") Long leaseId,
            @PathVariable("status") Status status){
        LeaseDto updatedLease = leaseService.updateStatus(leaseId, status);
        return new ResponseEntity<>(new ApiResponse<>("success",updatedLease),
                HttpStatus.OK);

    }

    /**
     * Update the ending date of a lease.
     *
     * @param leaseId the unique identifier of the lease to update
     * @param endingDate the new ending date for the lease (must be a valid future date)
     * @return ResponseEntity containing an ApiResponse with the updated LeaseDto and HTTP status
     */
    @PutMapping(path = "update/end/date/{id}/{date}")
    public ResponseEntity<ApiResponse<LeaseDto>>  updateDate(
            @PathVariable("id") Long leaseId,
            @PathVariable("date")LocalDate endingDate
            ){
        LeaseDto updatedDateLease = leaseService.updateDate(leaseId, endingDate);
        return ResponseEntity.ok(new ApiResponse<>("success", updatedDateLease));
    }


    /**
     * Update the rent amount for a lease.
     *
     * @param leaseId the unique identifier of the lease to update
     * @param rentAmount the new rent amount to set
     * @return ResponseEntity containing an ApiResponse with the updated LeaseDto and HTTP status
     */
    @PutMapping(path = "/update/rent/{id}/{amount}")
    public ResponseEntity<ApiResponse<LeaseDto>> updateRentAmount(
            @PathVariable("id") Long leaseId,
            @PathVariable("amount") Double rentAmount
    ) {
        LeaseDto updatedLease = leaseService.updateRentAmount(leaseId, rentAmount);
        return ResponseEntity.ok(new ApiResponse<>("Rent amount updated successfully", updatedLease));
    }

    //@DeleteMapping(path = "delete")


}
