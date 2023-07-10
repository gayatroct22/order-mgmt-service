package com.assignment.ordermanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByInstrumentIdAndType(String instrumentId, OrderType type);
    List<Order> findByIsOrderComplete(boolean isOrderComplete);
}
