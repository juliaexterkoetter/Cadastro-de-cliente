package com.example.cliente.controller;

import com.example.cliente.model.Customer;
import com.example.cliente.repository.CustomerRepository;
import com.example.cliente.service.CustomerService;
import com.example.cliente.util.CustomerMockUtils;
import com.example.cliente.exception.NotFoundException;
import com.example.cliente.exception.InternalServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Testa a criação de um cliente deve retornar HTTP 201 CREATED")
    void testCriarCustomer() throws Exception {
        Customer customer = CustomerMockUtils.criarCustomer();

        when(customerService.registerCustomer(any())).thenReturn(CustomerMockUtils.criarCustomerResponse());

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Nome Teste da Silva"))
                .andExpect(jsonPath("$.email").value("email@gmail.com"))
                .andExpect(jsonPath("$.phone").value("(99) 9999-9999"))
                .andExpect(jsonPath("$.documentNumber").value("999.999.999-99"));

    }
    
    @Test
    @DisplayName("Testa a criação de um cliente com dados inválidos deve retornar HTTP 400 BAD REQUEST")
    void testCriarCustomerComDadosInvalidos() throws Exception {
        Customer customer = CustomerMockUtils.criarCustomer();
        
        customer.setName(""); // Nome inválido
    customer.setEmail("email_invalido"); // Email inválido
    
    when(customerService.registerCustomer(any())).thenThrow(new IllegalArgumentException("Dados inválidos"));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Testa a atualização de um cliente deve retornar HTTP 200 OK")
    void testAtualizarCustomer() throws Exception {
        Customer updatedCustomer = CustomerMockUtils.criarCustomer();
        updatedCustomer.setName("Nome Atualizado");
        updatedCustomer.setEmail("email.atualizado@gmail.com");

        when(customerService.updateCustomer(eq(1L), any())).thenReturn(updatedCustomer);

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Nome Atualizado"))
                .andExpect(jsonPath("$.email").value("email.atualizado@gmail.com"))
                .andExpect(jsonPath("$.phone").value("(99) 9999-9999"))
                .andExpect(jsonPath("$.documentNumber").value("999.999.999-99"));
    }
    
    @Test
    @DisplayName("Testa a atualização de um cliente inexistente deve retornar HTTP 404 NOT FOUND")
    void testAtualizarCustomerInexistente() throws Exception {
        Customer updatedCustomer = CustomerMockUtils.criarCustomer();
        updatedCustomer.setName("Nome Atualizado");
        updatedCustomer.setEmail("email.atualizado@gmail.com");

        when(customerService.updateCustomer(eq(1L), any())).thenThrow(new NotFoundException("Cliente não encontrado"));

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.[0]").value("Cliente não encontrado"));
    }
    
    @Test
    @DisplayName("Testa a busca de um cliente por ID deve retornar HTTP 200 OK")
    void testBuscarCustomerPorId() throws Exception {
        Customer customer = CustomerMockUtils.criarCustomerResponse();

        when(customerService.findCustomerById(anyLong())).thenReturn(customer);

        mockMvc.perform(get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nome Teste da Silva"));
    }
    
    @Test
    @DisplayName("Testa a busca de um cliente inexistente por ID deve retornar HTTP 404 NOT FOUND")
    void testBuscarCustomerInexistentePorId() throws Exception {
        when(customerService.findCustomerById(anyLong())).thenThrow(new NotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value("Cliente não encontrado"));
    }
    
    @Test
    @DisplayName("Testa a busca de todos os clientes deve retornar HTTP 200 OK")
    void testBuscarTodosCustomers() throws Exception {
        Customer customer = CustomerMockUtils.criarCustomerResponse();
        List<Customer> customers = Collections.singletonList(customer);

        when(customerService.findAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nome Teste da Silva"));
    }
    
    @Test
    @DisplayName("Testa a deleção de um cliente deve retornar HTTP 204 NO CONTENT")
    void testDeletarCustomer() throws Exception {
        Mockito.doNothing().when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("Testa a deleção de um cliente inexistente deve retornar HTTP 404 NOT FOUND")
    void testDeletarCustomerInexistente() throws Exception {
        Mockito.doThrow(new NotFoundException("Cliente não encontrado")).when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value("Cliente não encontrado"));
    }
    
    @Test
    @DisplayName("Testa erro interno do servidor deve retornar HTTP 500 INTERNAL SERVER ERROR")
    void testErroInternoDoServidor() throws Exception {
        when(customerService.findCustomerById(anyLong())).thenThrow(new InternalServerErrorException("Erro interno do servidor"));

        mockMvc.perform(get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value("Erro interno do servidor"));
    }

}