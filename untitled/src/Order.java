import java.time.*;
import java.util.List;

public class Order {
    private int orderID;
    private String customerName;
    private List<Product> items;
    private Instant orderDate;
    private Instant pickupDate;

    // Constructor
    // Constructor
    public Order(int orderID, String customerName, List<Product> items, Instant orderDate, Instant pickupDate) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.items = items;
        this.orderDate = orderDate;
        this.pickupDate = pickupDate;
    }


    // Getters and Setters
        // OrderID
    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    // CustomerName
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Items
    public List<Product> getItems() {
        return items;
    }
    public void setItems(List<Product> items) {
        this.items = items;
    }

    // OrderDate
    public Instant getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    // PickupDate
    public Instant getPickupDate() {
        return pickupDate;
    }
    public void setPickupDate(Instant pickupDate) {
        this.pickupDate = pickupDate;
    }

    // Method to calculate the total cost of the order
    public double getTotalCost() {
        double totalCost = 0;
        for (Product product : items) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }
}
