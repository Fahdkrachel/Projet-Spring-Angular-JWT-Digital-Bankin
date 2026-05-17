package com.krachelfahd.ebankingbackend.services;

import com.krachelfahd.ebankingbackend.dtos.*;
import com.krachelfahd.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.krachelfahd.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.krachelfahd.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    // ─── Customer operations ────────────────────────────────────────────────────
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<CustomerDTO> listCustomers();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    // ─── Account operations ─────────────────────────────────────────────────────
    CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
            throws CustomerNotFoundException;

    SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException;

    List<BankAccountDTO> listBankAccounts();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    // ─── Banking operations ──────────────────────────────────────────────────────
    void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException;

    // ─── History ────────────────────────────────────────────────────────────────
    List<OperationDTO> getAccountOperations(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException;
}
