package com.krachelfahd.ebankingbackend.dtos;

import com.krachelfahd.ebankingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String id;
    private Date createdAt;
    private Double balance;
    private AccountStatus status;
    private String currency;
    private String type; // "CurrentAccount" or "SavingAccount" – JSON discriminator
    private CustomerDTO customerDTO;
}
