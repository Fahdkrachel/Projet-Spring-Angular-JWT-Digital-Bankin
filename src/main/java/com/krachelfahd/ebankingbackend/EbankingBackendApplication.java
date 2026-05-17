package com.krachelfahd.ebankingbackend;

import com.krachelfahd.ebankingbackend.dtos.CustomerDTO;
import com.krachelfahd.ebankingbackend.exceptions.CustomerNotFoundException;
import com.krachelfahd.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {

            Stream.of("Hassan", "Imane", "Mohamed").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name.toLowerCase() + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(
                            Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(
                            Math.random() * 120000, 5.5, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            bankAccountService.listBankAccounts().forEach(account -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        String accountId = account.getId();
                        bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit");
                        bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        };
    }
}
