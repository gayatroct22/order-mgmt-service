package com.assignment.ordermanagement;



import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instrumentId;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", instrumentId='" + instrumentId + '\'' +
                ", quantity=" + quantity +
                ", entryDate=" + entryDate +
                ", type=" + type +
                ", price=" + price +
                ", isOrderComplete=" + isOrderComplete +
                '}';
    }

    private int quantity;
    private LocalDateTime entryDate;
    private OrderType type;
    private double price;
    private Boolean isOrderComplete = false;


    // Custom Comparator for sorting Orders by entryDate
    public static Comparator<Order> entryDateComparator = Comparator.comparing(Order::getEntryDate);

    public void setId(Long id) {
        this.id = id;
    }

    public Order() {
    }

    public Order(Long id, String instrumentId, int quantity, LocalDateTime entryDate, OrderType type, double price) {
        this.id = id;
        this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.type = type;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getOrderComplete() {
        return isOrderComplete;
    }

    public void setOrderComplete(Boolean orderComplete) {
        isOrderComplete = orderComplete;
    }
}
