package com.example.cliente.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import com.example.cliente.model.Customer;
import com.example.cliente.exception.NotFoundException;
import com.example.cliente.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer registerCustomer(Customer customer) {
        customer.setRegistrationDate(LocalDate.now());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer existingCustomer = customerOptional.get();
            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setDocumentNumber(customer.getDocumentNumber());
            return customerRepository.save(existingCustomer);
        } else {
            throw new NotFoundException("Cliente não encontrado com o ID: " + id);
        }
    }

    @Cacheable(value = "customers", key = "#id")
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + id));
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Cliente não encontrado com o ID: " + id);
        }
        
        customerRepository.deleteById(id);
    }
}
