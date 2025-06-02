package com.banco.sistema_bancario.service;

import com.banco.sistema_bancario.dto.AccountDTO;
import com.banco.sistema_bancario.dto.TransactionDTO;
import com.banco.sistema_bancario.model.Account;
import com.banco.sistema_bancario.model.Customer;
import com.banco.sistema_bancario.model.Transaction;
import com.banco.sistema_bancario.repository.AccountRepository;
import com.banco.sistema_bancario.repository.CustomerRepository;
import com.banco.sistema_bancario.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository,
            CustomerRepository customerRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public Account createAccount(AccountDTO accountDTO) {
        // Validate customer ID
        if (accountDTO.getCustomerId() == null || accountDTO.getCustomerId() < 1
                || accountDTO.getCustomerId() > 99999) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        // Validate account type
        if (accountDTO.getAccountType() == null ||
                (!accountDTO.getAccountType().equals("S") && !accountDTO.getAccountType().equals("C"))) {
            throw new IllegalArgumentException("Account type must be 'S' for Savings or 'C' for Checking");
        }

        Customer customer = customerRepository.findById(accountDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Account.AccountType type = accountDTO.getAccountType().equals("S") ? Account.AccountType.SAVINGS
                : Account.AccountType.CHECKING;

        Account account = new Account(customer, type);
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    @Transactional
    public Transaction processDeposit(TransactionDTO transactionDTO) {
        validateTransactionAmount(transactionDTO.getAmount());

        Account account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Create transaction
        Transaction transaction = new Transaction(
                account,
                Transaction.TransactionType.DEPOSIT,
                transactionDTO.getAmount());

        // Update account balance
        BigDecimal newBalance = account.getBalance().add(transactionDTO.getAmount());
        account.setBalance(newBalance);

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        // Check minimum amount
        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.01");
        }

        // Check maximum amount
        if (amount.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new IllegalArgumentException("Amount cannot exceed 999999.99");
        }
    }

    @Transactional
    public Transaction processWithdrawal(TransactionDTO transactionDTO) {
        validateTransactionAmount(transactionDTO.getAmount());

        Account account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Check if account has sufficient balance
        if (account.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Create transaction
        Transaction transaction = new Transaction(
                account,
                Transaction.TransactionType.WITHDRAWAL,
                transactionDTO.getAmount());

        // Update account balance
        BigDecimal newBalance = account.getBalance().subtract(transactionDTO.getAmount());
        account.setBalance(newBalance);

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }
}