package com.assignment.ordermanagement;

// Execution.java
public class Execution {
    private String instrumentId;
    private int quantity;
    private ExecutionType type;
    private double price;

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

    public ExecutionType getType() {
        return type;
    }

    public void setType(ExecutionType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    // Constructors, getters, and setters
}
