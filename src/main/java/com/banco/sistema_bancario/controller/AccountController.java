package com.banco.sistema_bancario.controller;

import com.banco.sistema_bancario.dto.AccountResponseDTO;
import com.banco.sistema_bancario.dto.TransactionDTO;
import com.banco.sistema_bancario.dto.TransactionResponseDTO;
import com.banco.sistema_bancario.model.Account;
import com.banco.sistema_bancario.model.Transaction;
import com.banco.sistema_bancario.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> obterConta(@PathVariable("accountId") Long id) {
        return accountService.findById(id)
                .map(conta -> {
                    AccountResponseDTO dto = new AccountResponseDTO(
                        conta.getId(),
                        conta.getAccountType().toString(),
                        conta.getBalance()
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<?> sacar(@PathVariable("accountId") Long id, @RequestBody TransactionDTO transactionDTO) {
        try {
            // Garantir que o ID da conta no path é o mesmo do DTO
            if (transactionDTO.getAccountId() == null) {
                transactionDTO = new TransactionDTO(id, transactionDTO.getAmount());
            } else if (!transactionDTO.getAccountId().equals(id)) {
                return ResponseEntity.badRequest().body("ID da conta não correspondente");
            }
            
            Transaction transaction = accountService.processWithdrawal(transactionDTO);
            
            TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                transaction.getAccount().getId(),
                transaction.getAmount(),
                transaction.getAccount().getBalance(),
                transaction.getTimestamp()
            );
            
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}