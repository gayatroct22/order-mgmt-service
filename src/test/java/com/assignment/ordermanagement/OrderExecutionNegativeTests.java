package com.assignment.ordermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderExecutionNegativeTests {

    @Mock
    private OrderRepository orderRepository;


    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
    }


    @Test
    void testExecuteOrder_BookClosed() {
        // Arrange
        orderService.closeOrderBook();
        Execution execution = createExecution();

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> orderService.executeOrder(execution));
    }
    @Test
    void testExecuteOrder_NoMatchingOrders_Buy() {
        // Arrange
        orderService.openOrderBook();
        Execution execution = createExecution();
        when(orderRepository.findByInstrumentIdAndType(anyString(), any(OrderType.class))).thenReturn(Collections.emptyList());

        // Act and Assert
        List<Order> completedOrders = orderService.executeOrder(execution);
        assertTrue(completedOrders.isEmpty());
    }

    @Test
    void testExecuteOrder_NoMatchingOrders_Sell() {

        orderService.openOrderBook();
        Execution execution = createExecution();
        execution.setType(ExecutionType.ASK);
        when(orderRepository.findByInstrumentIdAndType(anyString(), any(OrderType.class))).thenReturn(Collections.emptyList());

        List<Order> completedOrders = orderService.executeOrder(execution);
        assertTrue(completedOrders.isEmpty());
    }


    private Execution createExecution() {
        Execution execution = new Execution();
        execution.setInstrumentId("AAPL");
        execution.setQuantity(100);
        execution.setType(ExecutionType.OFFER);
        execution.setPrice(150.50);
        return execution;
    }

}
