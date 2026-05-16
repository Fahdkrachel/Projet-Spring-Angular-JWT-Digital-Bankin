package com.krachelfahd.ebankingbackend.entities;

import com.krachelfahd.ebankingbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4)

public class BankAccount {
    @Id
    private String id;
    private Date created;
    private Double  balance;
    private AccountStatus status;
    private String currency;
    @ManyToOne /*plusieurs comptes pour un client*/
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    List<Operation> operationList;



}
