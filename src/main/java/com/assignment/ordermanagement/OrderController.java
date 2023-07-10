package com.assignment.ordermanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        Order newOrder = orderService.addOrder(order);
        log.info("New order added : "+ newOrder);
        return ResponseEntity.ok("New order added successfully : "+newOrder.toString());
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(orderId, order);
        log.info("Order Updated : "+ updatedOrder.toString());
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        log.info("Order Deleted ID : "+ orderId);
        return ResponseEntity.ok("Order has been deleted successfully.");
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeOrder(@RequestBody Execution execution) {
        log.info("Execution received : "+ execution.toString());
        List<Order> completed_orders = orderService.executeOrder(execution);
        log.info("Satisfied order list : "+ completed_orders);
        return ResponseEntity.ok("Order execution completed. No of orders satisfied :" + completed_orders.size());
    }

    @PostMapping("/closeOrderBook")
    public ResponseEntity<String> closeOrderBook() {
        orderService.closeOrderBook();
        log.info("Order book has been closed.");
        return ResponseEntity.ok("Order book has been closed.");
    }

    @PostMapping("/openOrderBook")
    public ResponseEntity<String> openOrderBook() {
        orderService.openOrderBook();
        log.info("Order book has been opened.");
        return ResponseEntity.ok("Order book has been opened.");
    }

    @GetMapping("/orderbook")
    public ResponseEntity<List<Order>> getOrderBook() {
        List<Order> orderBook = orderRepository.findAll();
        log.info("Order book : "+ orderBook);
        return ResponseEntity.ok(orderBook);
    }
}
