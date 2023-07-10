package com.assignment.ordermanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private Boolean isOrderBookClosed = true;

    public Order addOrder(Order order) {
        if (!isOrderBookClosed()) {
            Order temp = orderRepository.save(order);
            return temp;
        } else {
            log.error("Order book is closed. No new orders can be added.");
            throw new IllegalStateException("Order book is closed. No new orders can be added.");
        }
    }

    public Order updateOrder(Long orderId, Order order) {
        if (!isOrderBookClosed()) {
            Order existingOrder = getOrderById(orderId);
            if (existingOrder != null) {
                existingOrder.setInstrumentId(order.getInstrumentId());
                existingOrder.setQuantity(order.getQuantity());
                existingOrder.setEntryDate(order.getEntryDate());
                existingOrder.setType(order.getType());
                existingOrder.setPrice(order.getPrice());
                return orderRepository.save(existingOrder);
            } else {
                log.info("Invalid order ID: " + orderId);
                throw new IllegalArgumentException("Invalid order ID: " + orderId);
            }
        } else {
            log.error("Order book is closed. No orders can be updated.");
            throw new IllegalStateException("Order book is closed. No orders can be updated.");
        }
    }

    public void deleteOrder(Long orderId) {
        if (!isOrderBookClosed()) {
            Order existingOrder = getOrderById(orderId);
            if (existingOrder != null) {
                orderRepository.delete(existingOrder);
            } else {
                log.info("Invalid order ID: " + orderId);
                throw new IllegalArgumentException("Invalid order ID: " + orderId);
            }
        } else {
            throw new IllegalStateException("Order book is closed. No orders can be deleted.");
        }
    }

    public List<Order> executeOrder(Execution execution) {
        if (!isOrderBookClosed()) {
            List<Order> matchingOrders = orderRepository.findByInstrumentIdAndType(
                    execution.getInstrumentId(),
                    getMatchingOrderType(execution.getType()));//.stream().sorted(Order.entryDateComparator).collect(Collectors.toList());

           //Sort order in ascending order to distribute execution quantity as per priority of EntryDate
            List<Order> sortedOrders = matchingOrders.stream()
                    .sorted(Order.entryDateComparator)
                    .collect(Collectors.toList());

            double executionQuantity = execution.getQuantity();
            double executionPrice = execution.getPrice();

            if(execution.getType().equals(ExecutionType.OFFER)) {
                applyBuyRules(sortedOrders, executionQuantity, executionPrice);
            }
            else{
                applySellRules(sortedOrders, executionQuantity, executionPrice);
            }
            //Get current status of order Book to see completed orders.
            return  findCompletedOrders();

        } else {
            throw new IllegalStateException("Order book is closed. No orders can be executed.");
        }
    }

    private void applyBuyRules(List<Order> matchingOrders, double executionQuantity, double executionPrice) {
        for (Order order : matchingOrders) {
            //Order with price lower (buy) than the execution price is not considered.
            if (order.getPrice() > executionPrice) {
                if (order.getQuantity() <= executionQuantity) {
                    // Complete the entire order
                    executionQuantity -= order.getQuantity();
                    order.setOrderComplete(true);
                    updateOrder(order.getId(),order);// update order as complete
                } else {
                    log.info("Order can not be completed as quantity does not match. Order id :"+order.getId());
                }
            }else{
                log.debug("Order can not be completed as BUY price is lower than OFFER price");
            }
            if (executionQuantity == 0) {
                break;
            }
        }
    }

    private void applySellRules(List<Order> matchingOrders, double executionQuantity, double executionPrice) {
        for (Order order : matchingOrders) {
            //Order with price higher (sell) than the execution price is not considered
            if (order.getPrice() < executionPrice) {
                if (order.getQuantity() <= executionQuantity) {
                    // Complete the entire order
                    executionQuantity -= order.getQuantity();
                    order.setOrderComplete(true);
                    updateOrder(order.getId(),order);// update order as complete
                } else {
                    log.info("Order can not be completed as quantity does not match. Order id :"+order.getId());
                }
            }else{
                log.info("Order can not be completed as SELL price is higher than ASK . Order Id : "+order.getId());
            }
            if (executionQuantity == 0) {
                break;
            }
        }
    }

    public void closeOrderBook() {
        this.isOrderBookClosed = true;
    }

    public void openOrderBook() {
       this.isOrderBookClosed = false;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
    }

    private OrderType getMatchingOrderType(ExecutionType executionType) {
        return executionType == ExecutionType.OFFER ? OrderType.BUY : OrderType.SELL;
    }

    public boolean isOrderBookClosed() {
        return this.isOrderBookClosed;
    }
    public List<Order> findCompletedOrders() {
        return orderRepository.findByIsOrderComplete(true);
    }
}
