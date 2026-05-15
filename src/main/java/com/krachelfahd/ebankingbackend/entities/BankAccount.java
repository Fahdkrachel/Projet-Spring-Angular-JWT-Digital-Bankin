package com.krachelfahd.ebankingbackend.entities;

import com.krachelfahd.ebankingbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor

public class BankAccount {
    private String id;
    private Date created;
    private Double  balance;
    private AccountStatus status;
    private String currency;
    private Customer customer;
    List<Operation> operationList;



}
