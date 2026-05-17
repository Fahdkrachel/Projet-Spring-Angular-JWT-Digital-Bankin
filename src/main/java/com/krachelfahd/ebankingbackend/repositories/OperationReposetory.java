package com.krachelfahd.ebankingbackend.repositories;

import com.krachelfahd.ebankingbackend.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationReposetory extends JpaRepository<Operation,Long> {
}
