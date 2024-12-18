package com.example.Elite.Edge.Properties.service;


import com.example.Elite.Edge.Properties.constants.PaymentStatus;
import com.example.Elite.Edge.Properties.dto.PaymentDto;
import com.example.Elite.Edge.Properties.exceptions.LeaseNotFoundException;
import com.example.Elite.Edge.Properties.exceptions.PaymentNotFoundException;
import com.example.Elite.Edge.Properties.mapper.PaymentMapper;
import com.example.Elite.Edge.Properties.model.Lease;
import com.example.Elite.Edge.Properties.model.Payments;
import com.example.Elite.Edge.Properties.model.Property;
import com.example.Elite.Edge.Properties.repository.LeaseRepository;
import com.example.Elite.Edge.Properties.repository.PaymentRepository;
import com.example.Elite.Edge.Properties.repository.PropertyRepository;
import jakarta.el.PropertyNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;

    public PaymentService(PaymentRepository paymentRepository,
                           LeaseRepository leaseRepository,
                            PropertyRepository propertyRepository){
        this.paymentRepository = paymentRepository;
        this.leaseRepository = leaseRepository;
        this.propertyRepository = propertyRepository;
    }
    public List<PaymentDto> leasePayments(Long leaseId) {
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new LeaseNotFoundException("lease with id: " + leaseId + " does not exist"));


        List<PaymentDto> leasePaym = Optional.ofNullable(lease.getPayments())
                .orElseThrow(() -> new PaymentNotFoundException("There are no payments for this lease"))
                .stream()
                .map(PaymentDto::new).
                toList();

        if (leasePaym.isEmpty()) {
            //when a lease has no payments assigned to it yet
            throw new PaymentNotFoundException("There was an error processing your request");
        }

        return leasePaym;

    }

    @Transactional
    public PaymentMapper paymentHistory(Long propertyId, Integer pageNo, Integer pageSize){
        //ensure user selects a valid property
        Property fetchProperty = propertyRepository.findById(propertyId)
                .orElseThrow(()-> new PropertyNotFoundException("please select a valid property"));
        Pageable pageable = PageRequest.of(pageNo, pageSize);


        Page<Payments> payments = paymentRepository.findPaymentsByPropertyAndStatus(fetchProperty.getId(),
                pageable);

        if (payments.isEmpty()){
            throw new PaymentNotFoundException("There were no payments found for this property");
        }

        //unpack the page object to get the payments
        //only return the active payments

        List<PaymentDto> paymList= payments.getContent().
                stream().
                filter(payments1 -> payments1.getStatus().equals(PaymentStatus.SUCCESS))
                .map(p->new PaymentDto(p))
                .toList();
        log.info("--------------------------------------------------------");
        log.info("the current payment list is " + paymList);


        PaymentMapper paymentMapper = new PaymentMapper(
                paymList,
                payments.getNumber(),
                payments.getTotalElements(),
                payments.isLast()


        );
        return paymentMapper;
    }



}
