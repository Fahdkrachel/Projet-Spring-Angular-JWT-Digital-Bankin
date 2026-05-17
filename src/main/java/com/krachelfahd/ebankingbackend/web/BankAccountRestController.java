package com.krachelfahd.ebankingbackend.web;

import com.krachelfahd.ebankingbackend.dtos.*;
import com.krachelfahd.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.krachelfahd.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.krachelfahd.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BankAccountRestController {

    private final BankAccountService bankAccountService;

    // ─── Account queries ────────────────────────────────────────────────────────

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.listBankAccounts();
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable String accountId) {
        try {
            return ResponseEntity.ok(bankAccountService.getBankAccount(accountId));
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ─── Operations ─────────────────────────────────────────────────────────────

    @GetMapping("/accounts/{accountId}/operations")
    public List<OperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.getAccountOperations(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public ResponseEntity<AccountHistoryDTO> getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(bankAccountService.getAccountHistory(accountId, page, size));
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/accounts/debit")
    public ResponseEntity<DebitDTO> debit(@RequestBody DebitDTO debitDTO) {
        try {
            bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
            return ResponseEntity.ok(debitDTO);
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BalanceNotSufficientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/accounts/credit")
    public ResponseEntity<CreditDTO> credit(@RequestBody CreditDTO creditDTO) {
        try {
            bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
            return ResponseEntity.ok(creditDTO);
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        try {
            bankAccountService.transfer(
                    transferRequestDTO.getAccountIdSource(),
                    transferRequestDTO.getAccountIdDestination(),
                    transferRequestDTO.getAmount(),
                    transferRequestDTO.getDescription());
            return ResponseEntity.ok().build();
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BalanceNotSufficientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
