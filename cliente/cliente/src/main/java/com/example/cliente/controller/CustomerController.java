package com.example.cliente.controller;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cliente.model.Customer;
import com.example.cliente.exception.BadRequestException;
import com.example.cliente.exception.InternalServerErrorException;
import com.example.cliente.exception.NotFoundException;
import com.example.cliente.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
        public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer newCustomer = customerService.registerCustomer(customer);
            return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
        } catch (BadRequestException ex) {
            throw new BadRequestException("Erro na requisição.");
        } catch (InternalServerErrorException ex) {
            throw new InternalServerErrorException("Erro interno no servidor.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (NotFoundException ex) {
            throw new NotFoundException("ID não encontrado.");
        } catch (BadRequestException ex) {
            throw new BadRequestException("Erro na requisição.");
        } catch (InternalServerErrorException ex) {
            throw new InternalServerErrorException("Erro interno no servidor.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.findCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (NotFoundException ex) {
            throw new NotFoundException("ID não encontrado.");
        } catch (BadRequestException ex) {
            throw new BadRequestException("Erro na requisição.");
        } catch (InternalServerErrorException ex) {
            throw new InternalServerErrorException("Erro interno no servidor.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAllCustomers() {
        try {
            List<Customer> customers = customerService.findAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (InternalServerErrorException ex) {
            throw new InternalServerErrorException("Erro interno no servidor.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            throw new NotFoundException("ID não encontrado.");
        } catch (InternalServerErrorException ex) {
            throw new InternalServerErrorException("Erro interno no servidor.");
        }
    }
}
