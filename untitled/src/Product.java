import java.time.Duration;
import java.time.LocalTime;

public class Product {
    // Private Variables
    private int id;
    private String name;
    private double price;
    private Duration manufactureTime;

    // Constructor
    public Product(int id, String name, double price, Duration manufacturerTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.manufactureTime = manufacturerTime;
    }

    // Copy Constructor
    public Product(Product source) {
        this.id = source.id;
        this.name = source.name;
        this.price = source.price;
        this.manufactureTime = source.manufactureTime;
    }

    // Getters and Setters
        // Id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
        // Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
        // Price
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    // ManufactureTime
    public Duration getManufactureTime() {
        return manufactureTime;
    }

    public void setManufactureTime(Duration manufactureTime) {
        this.manufactureTime = manufactureTime;
    }
}