package com.example.cliente.util;

import com.example.cliente.model.Customer;

public class CustomerMockUtils {

    public static Customer criarCustomer() {
        Customer customer = new Customer();

        customer.setId(1L);
        customer.setName("Nome Teste da Silva");
        customer.setEmail("email@gmail.com");
        customer.setPhone("(99) 9999-9999");
        customer.setDocumentNumber("999.999.999-99");

        return customer;
    }

    public static Customer criarCustomerResponse() {
        Customer customer = criarCustomer();
        customer.setId(1L);

        return customer;
    }
}
