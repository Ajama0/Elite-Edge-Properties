package com.example.Elite.Edge.Properties.mapper;

import com.example.Elite.Edge.Properties.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PaymentMapper {

    private List<PaymentDto> content;
    private int pageNo;
    private Long totalElements;
    private boolean isLast;




}
