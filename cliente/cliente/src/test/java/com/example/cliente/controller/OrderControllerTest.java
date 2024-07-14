package com.example.cliente.controller;

import com.example.cliente.exception.NotFoundException;
import com.example.cliente.model.Order;
import com.example.cliente.util.OrderMockUtils;
import com.example.cliente.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.order = OrderMockUtils.criarOrder();
    }

    @Test
    @DisplayName("Testa a criação de um pedido com sucesso")
    void testCreateOrderSuccess() throws Exception {
        when(orderService.createOrder(any())).thenReturn(OrderMockUtils.criarOrderResponse());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalValue").value(BigDecimal.valueOf(100.00)))
                .andExpect(jsonPath("$.status").value("NEW"));
    }
    
    @Test
    @DisplayName("Testa a busca de todos os pedidos com sucesso")
    void testGetAllOrdersSuccess() throws Exception {
        List<Order> orders = Collections.singletonList(OrderMockUtils.criarOrderResponse());

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalValue").value(BigDecimal.valueOf(100.00)))
                .andExpect(jsonPath("$[0].status").value("NEW"));
    }
    
    @Test
    @DisplayName("Testa a busca de um pedido por ID com sucesso")
    void testGetOrderByIdSuccess() throws Exception {
        when(orderService.getOrderById(anyLong())).thenReturn(Optional.of(OrderMockUtils.criarOrderResponse()));

        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalValue").value(BigDecimal.valueOf(100.00)))
                .andExpect(jsonPath("$.status").value("NEW"));
    }
    
    @Test
    @DisplayName("Testa a busca de um pedido inexistente por ID deve retornar HTTP 404 NOT FOUND")
    void testGetOrderByIdNotFound() throws Exception {
        when(orderService.getOrderById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value("Pedido não encontrado com o ID: 1"));
    }
    
    @Test
    @DisplayName("Testa a atualização de um pedido com sucesso")
    void testUpdateOrderSuccess() throws Exception {
        Order order = OrderMockUtils.criarOrder();
        order.setStatus("PAID");
        
        when(orderService.updateOrder(anyLong(), any())).thenReturn(order);

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalValue").value(BigDecimal.valueOf(100.00)))
                .andExpect(jsonPath("$.status").value("PAID"));
    }
    
    @Test
    @DisplayName("Testa a atualização de um pedido inexistente deve retornar HTTP 404 NOT FOUND")
    void testUpdateOrderNotFound() throws Exception {
        when(orderService.updateOrder(anyLong(), any())).thenReturn(null);

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value("Pedido não encontrado com o ID: 1"));
    }
    
    @Test
    @DisplayName("Testa a deleção de um pedido com sucesso")
    void testDeleteOrderSuccess() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(anyLong());

        mockMvc.perform(delete("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("Testa a deleção de um pedido inexistente deve retornar HTTP 404 NOT FOUND")
    void testDeleteOrderNotFound() throws Exception {
        Mockito.doThrow(new NotFoundException("Pedido não encontrado com o ID: 1")).when(orderService).deleteOrder(anyLong());

        mockMvc.perform(delete("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value("Pedido não encontrado com o ID: 1"));
    }
}
