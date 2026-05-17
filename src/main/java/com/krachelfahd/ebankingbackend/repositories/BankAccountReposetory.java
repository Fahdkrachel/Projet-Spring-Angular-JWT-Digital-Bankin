package com.krachelfahd.ebankingbackend.repositories;

import com.krachelfahd.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountReposetory extends JpaRepository<BankAccount,String> {
}
