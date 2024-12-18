package com.example.Elite.Edge.Properties.controller;


import com.example.Elite.Edge.Properties.dto.PaymentDto;
import com.example.Elite.Edge.Properties.mapper.PaymentMapper;
import com.example.Elite.Edge.Properties.service.PaymentService;
import com.example.Elite.Edge.Properties.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     *
     * @param propertyId
     * @param pageNo
     * @param pageSize
     * @return
     */

    @GetMapping(path = "history")
    public ResponseEntity<ApiResponse<PaymentMapper>> paymentHistory(
            @RequestParam("id") Long propertyId,
            @RequestParam(value = "pageNo", defaultValue = "0" , required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) Integer pageSize
    ){
        PaymentMapper paymHistory = paymentService.paymentHistory(propertyId, pageNo, pageSize);
        return ResponseEntity.ok(new ApiResponse<>("success", paymHistory));
    }



}
