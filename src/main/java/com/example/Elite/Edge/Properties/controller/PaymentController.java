package com.example.Elite.Edge.Properties.controller;


import com.example.Elite.Edge.Properties.dto.PaymentDto;
import com.example.Elite.Edge.Properties.dto.RequestPaymentDto;
import com.example.Elite.Edge.Properties.mapper.PaymentMapper;
import com.example.Elite.Edge.Properties.service.PaymentService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    /**
     * leasePayments: Retrieve payment information related to a lease
     * @param LeaseId : Represents the Lease contract we are querying for its payments
     * @return custom Api response wrapped in a 200 status code
     */
    @GetMapping(path = "/lease")
    public ResponseEntity<ApiResponse<List<PaymentDto>>> leasePayments(
            @RequestParam("id") Long LeaseId
    ) {
        List<PaymentDto> paymentDto = paymentService.leasePayments(LeaseId);
        return new ResponseEntity<>(new ApiResponse<>("success", paymentDto),
                HttpStatus.OK);

    }

    /**
     * Retrieve the payment history for a specific property.
     *
     * @param propertyId the unique identifier of the property whose payment history is being retrieved
     * @param pageNo the page number for paginated results; defaults to 0 if not provided
     * @param pageSize the number of records to include per page; defaults to 100 if not provided
     * @return ResponseEntity containing an ApiResponse with a PaymentMapper object,
     * which includes payment history details such as content, page number, total elements,
     * and pagination status, along with an HTTP status of OK
     */

    @GetMapping(path = "history")
    public ResponseEntity<ApiResponse<PaymentMapper>> paymentHistory(
            @RequestParam("id") Long propertyId,
            @RequestParam(value = "pageNo", defaultValue = "0" , required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) int pageSize
    ){
        PaymentMapper paymHistory = paymentService.paymentHistory(propertyId, pageNo, pageSize);
        return ResponseEntity.ok(new ApiResponse<>("success", paymHistory));
    }

    /**
     *
     * @param leaseId - used to identify which lease the payment is being created for
     * @param requestPaymentDto -hide internals and only give client what is required
     * @return custom api response and the transaction id/receipt
     */
    
    @PostMapping(path = "create")
    public ResponseEntity<ApiResponse<String>> createPayment(@RequestParam(value = "id") Long leaseId,
                                                             @RequestBody RequestPaymentDto requestPaymentDto){

        String transactionId = paymentService.createPayment(leaseId, requestPaymentDto);
        return new ResponseEntity<>(new ApiResponse<>("success", transactionId),
                HttpStatus.CREATED);
    }







}
