import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Invoice {
    private Order order;
    private WeekOpeningHours weekOpeningHours;
    private final Instant validPickupInstant;
    private CustomerInfo customerInfo; // Add this variable

    public Invoice(Order order, WeekOpeningHours weekOpeningHours, Instant validPickupInstant, CustomerInfo customerInfo) {
        this.order = order;
        this.weekOpeningHours = weekOpeningHours;
        this.validPickupInstant = validPickupInstant;
        this.customerInfo = customerInfo; // Initialize the customer information
    }


    @Override
    public String toString() {
        ZoneId timeZone = ZoneId.of("Europe/Amsterdam");

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("-------------------------------------------------\n");
        sb.append("             PhotoShop - Invoice                \n");
        sb.append("-------------------------------------------------\n");
        sb.append("Order ID: ").append(order.getOrderID()).append("\n");
        sb.append("Customer Name: ").append(order.getCustomerName()).append("\n");
        sb.append("Order Date: ").append(order.getOrderDate().atZone(timeZone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("Pickup Date: ").append(validPickupInstant.atZone(timeZone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("-------------------------------------------------\n");
        sb.append("                 Order Details                   \n");
        sb.append("-------------------------------------------------\n");
        for (Product product : order.getItems()) {
            sb.append(product.getName()).append(" - $").append(product.getPrice()).append("\n");
        }
        sb.append("-------------------------------------------------\n");
        sb.append("Total Cost: $").append(order.getTotalCost()).append("\n");
        sb.append("-------------------------------------------------\n");
        return sb.toString();
    }

}