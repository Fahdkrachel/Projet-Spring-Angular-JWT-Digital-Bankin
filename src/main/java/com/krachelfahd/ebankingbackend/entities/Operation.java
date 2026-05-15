package com.krachelfahd.ebankingbackend.entities;

import com.krachelfahd.ebankingbackend.enums.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor

public class Operation {
    private Long id;
    private Date date;
    private double amount;
    private OpType type;
    private BankAccount bankAccount;

}
