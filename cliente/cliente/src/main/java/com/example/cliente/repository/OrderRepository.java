package com.example.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cliente.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
