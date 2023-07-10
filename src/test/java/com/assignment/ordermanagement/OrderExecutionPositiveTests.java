package com.assignment.ordermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderExecutionPositiveTests {
    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void testExecuteOrder_BookOpen_MatchingOrdersExist_Buy() {
        // Mock data
        Execution execution = new Execution();
        execution.setInstrumentId("AAPL");
        execution.setQuantity(100);
        execution.setType(ExecutionType.OFFER);
        execution.setPrice(150.50);

        List<Order> matchingOrders = new ArrayList<>();
        // Populate matchingOrders with test data
        Order order1 = new Order();
        order1.setId(1L);
        order1.setInstrumentId("AAPL");
        order1.setQuantity(50);
        order1.setEntryDate(LocalDateTime.now());
        order1.setType(OrderType.BUY);
        order1.setPrice(160.0);
        matchingOrders.add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setInstrumentId("AAPL");
        order2.setQuantity(50);
        order2.setEntryDate(LocalDateTime.now().minusHours(1));
        order2.setType(OrderType.BUY);
        order2.setPrice(155.0);
        matchingOrders.add(order2);

        // Mock behavior
        when(orderRepository.findByInstrumentIdAndType(eq(execution.getInstrumentId()), any(OrderType.class)))
                .thenReturn(matchingOrders);
        when(orderRepository.save(any(Order.class))).thenReturn(null);
        when(orderRepository.findById(eq(1L))).thenReturn(Optional.of(order1));
        when(orderRepository.findById(eq(2L))).thenReturn(Optional.of(order2));
        when(orderRepository.findByIsOrderComplete(true)).thenReturn(matchingOrders);


        orderService.openOrderBook();
        // Invoke the method under test
        List<Order> completedOrders = orderService.executeOrder(execution);

        // Assertions
        // Verify the necessary methods were called with the expected arguments
        verify(orderRepository, times(1))
                .findByInstrumentIdAndType(eq(execution.getInstrumentId()), any(OrderType.class));
        verify(orderRepository, atLeastOnce()).save(any(Order.class));

        assertEquals(2, completedOrders.size(), "2 orders completed of type BUY");
    }

    @Test
    void testExecuteOrder_BookOpen_MatchingOrdersExist_Sell() {
        // Mock data
        Execution execution = new Execution();
        execution.setInstrumentId("AAPL");
        execution.setQuantity(100);
        execution.setType(ExecutionType.ASK);
        execution.setPrice(150.50);

        List<Order> matchingOrders = new ArrayList<>();
        // Populate matchingOrders with test data
        Order order1 = new Order();
        order1.setId(1L);
        order1.setInstrumentId("AAPL");
        order1.setQuantity(50);
        order1.setEntryDate(LocalDateTime.now());
        order1.setType(OrderType.SELL);
        order1.setPrice(100.0);
        matchingOrders.add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setInstrumentId("AAPL");
        order2.setQuantity(50);
        order2.setEntryDate(LocalDateTime.now().minusHours(1));
        order2.setType(OrderType.SELL);
        order2.setPrice(100.0);
        matchingOrders.add(order2);

        // Mock behavior
        when(orderRepository.findByInstrumentIdAndType(eq(execution.getInstrumentId()), any(OrderType.class)))
                .thenReturn(matchingOrders);
        when(orderRepository.save(any(Order.class))).thenReturn(null);
        when(orderRepository.findById(eq(1L))).thenReturn(Optional.of(order1));
        when(orderRepository.findById(eq(2L))).thenReturn(Optional.of(order2));
        when(orderRepository.findByIsOrderComplete(true)).thenReturn(matchingOrders);


        orderService.openOrderBook();
        // Invoke the method under test
        List<Order> completedOrders = orderService.executeOrder(execution);

        // Assertions
        // Verify the necessary methods were called with the expected arguments
        verify(orderRepository, times(1))
                .findByInstrumentIdAndType(eq(execution.getInstrumentId()), any(OrderType.class));
        verify(orderRepository, atLeastOnce()).save(any(Order.class));

        assertEquals(2, completedOrders.size(), "2 orders completed of type SELL");
    }
}
