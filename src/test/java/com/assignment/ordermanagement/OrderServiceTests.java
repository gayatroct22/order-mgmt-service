package com.assignment.ordermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testAddOrder() {


        Order order = new Order();

        order.setInstrumentId("AAPL");
        order.setQuantity(100);
        order.setEntryDate(LocalDateTime.now());
        order.setType(OrderType.BUY);
        order.setPrice(150.50);

        orderService.openOrderBook();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.addOrder(order);

        //assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testUpdateOrder() {
        Long orderId = 1L;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setInstrumentId("AAPL");
        existingOrder.setQuantity(100);
        existingOrder.setEntryDate(LocalDateTime.now());
        existingOrder.setType(OrderType.BUY);
        existingOrder.setPrice(150.50);

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setInstrumentId("AAPL");
        updatedOrder.setQuantity(200);
        updatedOrder.setEntryDate(LocalDateTime.now());
        updatedOrder.setType(OrderType.BUY);
        updatedOrder.setPrice(160.75);

        orderService.openOrderBook();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(orderId, updatedOrder);

        assertEquals(updatedOrder, result);

    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setInstrumentId("AAPL");
        existingOrder.setQuantity(100);
        existingOrder.setEntryDate(LocalDateTime.now());
        existingOrder.setType(OrderType.BUY);
        existingOrder.setPrice(150.50);

        orderService.openOrderBook();

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(existingOrder));

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(existingOrder);
    }

}
