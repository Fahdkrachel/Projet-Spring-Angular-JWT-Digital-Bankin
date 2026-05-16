package com.krachelfahd.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@DiscriminatorValue("CC")

public class CurrentAccount extends BankAccount{
    private double overDraft;
}
