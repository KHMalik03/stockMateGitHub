package fr.stockmate.java.models;

abstract class Order {
    protected int id;
    protected String orderDate;
    protected String deliveryDate;
    protected String orderStatus;
    protected double totalAmount;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

}