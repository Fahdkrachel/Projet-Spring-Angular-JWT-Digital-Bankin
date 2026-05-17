package com.krachelfahd.ebankingbackend.dtos;

import com.krachelfahd.ebankingbackend.enums.OpType;
import lombok.Data;

import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private OpType type;
    private String description;
}
