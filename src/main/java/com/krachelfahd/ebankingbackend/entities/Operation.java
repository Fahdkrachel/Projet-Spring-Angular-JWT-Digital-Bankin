package com.krachelfahd.ebankingbackend.entities;

import com.krachelfahd.ebankingbackend.enums.OpType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity

public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private double amount;
    private OpType type;
    @ManyToOne
    private BankAccount bankAccount;

}
