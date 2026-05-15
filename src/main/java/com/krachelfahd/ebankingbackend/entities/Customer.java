package com.krachelfahd.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data @AllArgsConstructor @NoArgsConstructor

public class Customer {
    private Long id;
    private String name;
    private String email;
    private List<BankAccount> bankAccounts;
}
