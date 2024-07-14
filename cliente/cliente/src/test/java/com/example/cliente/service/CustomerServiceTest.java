package com.example.cliente.service;

import com.example.cliente.exception.NotFoundException;
import com.example.cliente.model.Customer;
import com.example.cliente.repository.CustomerRepository;
import com.example.cliente.util.CustomerMockUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Testa a criação de um cliente com sucesso")
    void criarClienteSucesso() {
        Customer customer = CustomerMockUtils.criarCustomer();

        when(customerRepository.save(any())).thenReturn(CustomerMockUtils.criarCustomerResponse());

        Customer newCustomer = customerService.registerCustomer(customer);

        assertNotNull(newCustomer);
        assertEquals(1L, newCustomer.getId());
        assertEquals("Nome Teste da Silva", newCustomer.getName());
        assertEquals("email@gmail.com", newCustomer.getEmail());
        assertEquals("(99) 9999-9999", newCustomer.getPhone());
        assertEquals("999.999.999-99", newCustomer.getDocumentNumber());
    }
    
    @Test
    @DisplayName("Testa a atualização de um cliente com sucesso")
    void atualizarClienteSucesso() {
        Customer existingCustomer = CustomerMockUtils.criarCustomer();
        existingCustomer.setId(1L);
        Customer updatedCustomer = CustomerMockUtils.criarCustomer();
        
        updatedCustomer.setName("Nome Atualizado");
    updatedCustomer.setEmail("email.atualizado@gmail.com");

    when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
    when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

    Customer result = customerService.updateCustomer(1L, updatedCustomer);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Nome Atualizado", result.getName());
    assertEquals("email.atualizado@gmail.com", result.getEmail());
    assertEquals("(99) 9999-9999", result.getPhone()); // Presume que o telefone não foi atualizado
    assertEquals("999.999.999-99", result.getDocumentNumber()); // Presume que o documento não foi atualizado
    }
    
    @Test
    @DisplayName("Testa a atualização de um cliente inexistente deve lançar NotFoundException")
    void atualizarClienteInexistente() {
        Customer updatedCustomer = CustomerMockUtils.criarCustomer();
        updatedCustomer.setName("Nome Atualizado");
        updatedCustomer.setEmail("email.atualizado@gmail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.updateCustomer(1L, updatedCustomer);
        });

        assertEquals("Cliente não encontrado com o ID: 1", exception.getMessage());
    }
    
    @Test
    @DisplayName("Testa a busca de um cliente por ID com sucesso")
    void buscarClientePorIdSucesso() {
        Customer customer = CustomerMockUtils.criarCustomer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findCustomerById(1L);

        assertNotNull(foundCustomer);
        assertEquals(1L, foundCustomer.getId());
        assertEquals("Nome Teste da Silva", foundCustomer.getName());
        assertEquals("email@gmail.com", foundCustomer.getEmail());
        assertEquals("(99) 9999-9999", foundCustomer.getPhone());
        assertEquals("999.999.999-99", foundCustomer.getDocumentNumber());
    }
    
    @Test
    @DisplayName("Testa a busca de um cliente inexistente por ID deve lançar NotFoundException")
    void buscarClienteInexistentePorId() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.findCustomerById(1L);
        });

        assertEquals("Cliente não encontrado com o ID: 1", exception.getMessage());
    }
    
    @Test
    @DisplayName("Testa a busca de todos os clientes com sucesso")
    void buscarTodosClientesSucesso() {
        Customer customer1 = CustomerMockUtils.criarCustomer();
        customer1.setId(1L);

        Customer customer2 = CustomerMockUtils.criarCustomer();
        customer2.setId(2L);
        customer2.setName("Nome Teste 2");

        List<Customer> customerList = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> foundCustomers = customerService.findAllCustomers();

        assertNotNull(foundCustomers);
        assertEquals(2, foundCustomers.size());
        assertEquals(1L, foundCustomers.get(0).getId());
        assertEquals("Nome Teste da Silva", foundCustomers.get(0).getName());
        assertEquals(2L, foundCustomers.get(1).getId());
        assertEquals("Nome Teste 2", foundCustomers.get(1).getName());
    }
    
    @Test
    @DisplayName("Testa a deleção de um cliente com sucesso")
    void deletarClienteSucesso() {
        Customer customer = CustomerMockUtils.criarCustomer();
        customer.setId(1L);
        
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(customerRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> {
            customerService.deleteCustomer(1L);
        });

        verify(customerRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Testa a deleção de um cliente inexistente deve lançar NotFoundException")
    void deletarClienteInexistente() {
        when(customerRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("Cliente não encontrado com o ID: 1", exception.getMessage());
    }

}