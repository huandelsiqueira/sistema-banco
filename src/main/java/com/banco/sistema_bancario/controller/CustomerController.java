package com.banco.sistema_bancario.controller;

import com.banco.sistema_bancario.dto.CustomerDTO;
import com.banco.sistema_bancario.model.Customer;
import com.banco.sistema_bancario.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> obterCliente(@PathVariable("customerId") Long id) {
        return customerService.findById(id)
                .map(cliente -> {
                    // Converter LocalDate para String no formato YYYYMMDD
                    String birthday = cliente.getBirthday().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    
                    CustomerDTO dto = new CustomerDTO(
                        cliente.getName(),
                        cliente.getEmail(),
                        birthday
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}