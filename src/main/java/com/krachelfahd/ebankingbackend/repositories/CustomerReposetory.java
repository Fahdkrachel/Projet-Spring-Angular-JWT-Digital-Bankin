package com.krachelfahd.ebankingbackend.repositories;

import com.krachelfahd.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerReposetory extends JpaRepository<Customer,Long> {

}
