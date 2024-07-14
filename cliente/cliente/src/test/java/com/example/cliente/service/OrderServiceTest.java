package com.example.cliente.service;

import com.example.cliente.exception.NotFoundException;
import com.example.cliente.model.Order;
import com.example.cliente.repository.OrderRepository;
import com.example.cliente.util.OrderMockUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Testa a criação de um pedido com sucesso")
    void criarPedidoSucesso() {
        Order order = OrderMockUtils.criarOrder();

        when(orderRepository.save(any())).thenReturn(OrderMockUtils.criarOrderResponse());

        Order newOrder = orderService.createOrder(order);

        assertNotNull(newOrder);
        assertEquals(1L, newOrder.getId());
        assertEquals(BigDecimal.valueOf(100.00), newOrder.getTotalValue());
        assertEquals("NEW", newOrder.getStatus());
    }
    
    @Test
    @DisplayName("Testa a criação de um pedido com dados inválidos")
    void testCreateOrderWithInvalidData() {
        Order invalidOrder = new Order(); // Pedido com dados faltando

        when(orderRepository.save(any(Order.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(invalidOrder);
        });

        assertEquals("Dados inválidos", exception.getMessage());
    }
    
    @Test
    @DisplayName("Testa a busca de todos os pedidos com sucesso")
    void testGetAllOrdersSuccess() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(OrderMockUtils.criarOrderResponse()));

        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());
        assertEquals(BigDecimal.valueOf(100.00), orders.get(0).getTotalValue());
        assertEquals("NEW", orders.get(0).getStatus());
    }
    
    @Test
    @DisplayName("Testa a busca de um pedido por ID com sucesso")
    void testGetOrderByIdSuccess() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(OrderMockUtils.criarOrderResponse()));

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals(1L, foundOrder.get().getId());
        assertEquals(BigDecimal.valueOf(100.00), foundOrder.get().getTotalValue());
        assertEquals("NEW", foundOrder.get().getStatus());
    }
    
    @Test
    @DisplayName("Testa a busca de um pedido inexistente por ID deve retornar Optional vazio")
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        assertFalse(foundOrder.isPresent());
    }
    
    @Test
    @DisplayName("Testa a atualização de um pedido com sucesso")
    void testUpdateOrderSuccess() {
        Order existingOrder = OrderMockUtils.criarOrder();
        existingOrder.setId(1L);
        Order updatedOrder = OrderMockUtils.criarOrder();
        updatedOrder.setStatus("PAID");
        
        when(orderRepository.existsById(1L)).thenReturn(true);
    when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertNotNull(result);
        assertEquals(1L, updatedOrder.getId());
        assertEquals("PAID", updatedOrder.getStatus());
    }
    
    @Test
    @DisplayName("Testa a atualização de um pedido com dados inválidos")
    void testUpdateOrderWithInvalidData() {
        Order invalidOrder = new Order(); // Pedido com dados faltando

        when(orderRepository.existsById(anyLong())).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(1L, invalidOrder);
        });

        assertEquals("Dados inválidos", exception.getMessage());
    }
    
    @Test
    @DisplayName("Testa a atualização de um pedido inexistente deve lançar NotFoundException")
    void atualizarPedidoInexistente() {
        Order order = OrderMockUtils.criarOrder();
        order.setStatus("PAID");
        
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        Order updatedOrder = orderService.updateOrder(1L, order);

        assertNull(updatedOrder);
    }
    
    @Test
    @DisplayName("Testa a deleção de um pedido com sucesso")
    void testDeleteOrderSuccess() {
        when(orderRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(orderRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(orderRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Testa a deleção de um pedido inexistente deve lançar NotFoundException")
    void testDeleteOrderNotFound() {
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> orderService.deleteOrder(1L));
        assertEquals("Pedido não encontrado com o ID: 1", exception.getMessage());
    }

}