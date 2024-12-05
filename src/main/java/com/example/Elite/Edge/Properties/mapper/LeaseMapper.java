package com.example.Elite.Edge.Properties.mapper;

import com.example.Elite.Edge.Properties.dto.LeaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaseMapper {

    private List<LeaseDto> content;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    private Boolean last;
}
