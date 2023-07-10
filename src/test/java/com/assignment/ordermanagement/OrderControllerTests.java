package com.assignment.ordermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderControllerTests {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrder() {
        Order order = new Order();
        when(orderService.addOrder(any(Order.class))).thenReturn(order);

        ResponseEntity<String> response = orderController.addOrder(order);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New order added successfully : "+order.toString(), response.getBody());
        verify(orderService, times(1)).addOrder(order);
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        Long orderId = 1L;
        when(orderService.updateOrder(eq(orderId), any(Order.class))).thenReturn(order);

        ResponseEntity<Order> response = orderController.updateOrder(orderId, order);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).updateOrder(orderId, order);
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        ResponseEntity<String> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order has been deleted successfully.", response.getBody());
        verify(orderService, times(1)).deleteOrder(orderId);
    }

   //ExecuteOrder test cases covered in separate class.

    @Test
    public void testCloseOrderBook() {
        ResponseEntity<String> response = orderController.closeOrderBook();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order book has been closed.", response.getBody());
        verify(orderService, times(1)).closeOrderBook();
    }

    @Test
    public void testOpenOrderBook() {
        ResponseEntity<String> response = orderController.openOrderBook();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order book has been opened.", response.getBody());
        verify(orderService, times(1)).openOrderBook();
    }

    @Test
    public void testGetOrderBook() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orderList = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orderList);

        ResponseEntity<List<Order>> response = orderController.getOrderBook();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderList, response.getBody());
        verify(orderRepository, times(1)).findAll();
    }
}
