package com.example.cliente.util;

import com.example.cliente.model.Order;

import java.math.BigDecimal;

public class OrderMockUtils {

    public static Order criarOrder() {
        Order order = new Order();

        order.setId(1L);
        order.setTotalValue(BigDecimal.valueOf(100.00));
        order.setStatus("NEW");

        return order;
    }

    public static Order criarOrderResponse() {
        Order order = criarOrder();
        order.setId(1L);

        return order;
    }
}
