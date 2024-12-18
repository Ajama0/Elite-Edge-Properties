package com.example.Elite.Edge.Properties.mapper;

import com.example.Elite.Edge.Properties.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMapper {
    private List<PaymentDto> content;
    private Integer pageNo;
    private Long totalElements;
    private Boolean isLast;




}
