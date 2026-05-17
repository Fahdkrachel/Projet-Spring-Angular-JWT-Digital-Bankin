package com.krachelfahd.ebankingbackend.services;

import com.krachelfahd.ebankingbackend.dtos.*;
import com.krachelfahd.ebankingbackend.entities.*;
import com.krachelfahd.ebankingbackend.enums.AccountStatus;
import com.krachelfahd.ebankingbackend.enums.OpType;
import com.krachelfahd.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.krachelfahd.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.krachelfahd.ebankingbackend.exceptions.CustomerNotFoundException;
import com.krachelfahd.ebankingbackend.mappers.BankAccountMapperImpl;
import com.krachelfahd.ebankingbackend.repositories.BankAccountReposetory;
import com.krachelfahd.ebankingbackend.repositories.CustomerReposetory;
import com.krachelfahd.ebankingbackend.repositories.OperationReposetory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final CustomerReposetory customerRepository;
    private final BankAccountReposetory bankAccountRepository;
    private final OperationReposetory operationRepository;
    private final BankAccountMapperImpl mapper;

    // ──────────────────────────────────────────────────────────────────────────────
    // Customer operations
    // ──────────────────────────────────────────────────────────────────────────────

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer: {}", customerDTO.getName());
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer: {}", customerDTO.getName());
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.info("Deleting customer: {}", customerId);
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
        return mapper.fromCustomer(customer);
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // Account creation
    // ──────────────────────────────────────────────────────────────────────────────

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreated(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCurrency("MAD");
        currentAccount.setCustomer(customer);

        CurrentAccount savedAccount = (CurrentAccount) bankAccountRepository.save(currentAccount);
        return mapper.fromCurrentAccount(savedAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreated(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterstrate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCurrency("MAD");
        savingAccount.setCustomer(customer);

        SavingAccount savedAccount = (SavingAccount) bankAccountRepository.save(savingAccount);
        return mapper.fromSavingAccount(savedAccount);
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // Account queries
    // ──────────────────────────────────────────────────────────────────────────────

    @Override
    public List<BankAccountDTO> listBankAccounts() {
        return bankAccountRepository.findAll()
                .stream()
                .map(account -> {
                    if (account instanceof CurrentAccount ca) {
                        return mapper.fromCurrentAccount(ca);
                    } else {
                        return mapper.fromSavingAccount((SavingAccount) account);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found: " + accountId));
        if (bankAccount instanceof CurrentAccount ca) {
            return mapper.fromCurrentAccount(ca);
        }
        return mapper.fromSavingAccount((SavingAccount) bankAccount);
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // Banking operations
    // ──────────────────────────────────────────────────────────────────────────────

    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found: " + accountId));

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Insufficient balance");
        }

        Operation operation = new Operation();
        operation.setDate(new Date());
        operation.setAmount(amount);
        operation.setType(OpType.DEBIT);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found: " + accountId));

        Operation operation = new Operation();
        operation.setDate(new Date());
        operation.setAmount(amount);
        operation.setType(OpType.CREDIT);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, description);
        credit(accountIdDestination, amount, description);
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // History
    // ──────────────────────────────────────────────────────────────────────────────

    @Override
    public List<OperationDTO> getAccountOperations(String accountId) {
        return operationRepository.findByBankAccountId(accountId)
                .stream()
                .map(mapper::fromOperation)
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found: " + accountId));

        Page<Operation> accountOperations = operationRepository.findByBankAccountId(accountId,
                PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setOperationDTOS(
                accountOperations.getContent().stream()
                        .map(mapper::fromOperation)
                        .collect(Collectors.toList()));
        return accountHistoryDTO;
    }
}
