import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> cartItems = new ArrayList<>();

    // Method to add a product to the shopping cart
    public void addProduct(Product product) {
        cartItems.add(product);
    }

    // Method to remove a product from the shopping cart
    public boolean removeProduct(int productId) {
        for (Product product : cartItems) {
            if (product.getId() == productId) {
                cartItems.remove(product);
                return true;
            }
        }
        return false;
    }

    // Method to clear the entire shopping cart
    public void clearCart() {
        cartItems.clear();
    }

    // Method to get the list of products in the shopping cart
    public List<Product> getItems() {
        return cartItems;
    }

    // Method to check if the shopping cart is empty
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    // Calculate the total manufacturing time for all products in the cart
//    public Duration getTotalManufacturingTime() {
//        Duration totalDuration = Duration.ZERO;
//        for (Product product : cartItems) {
//            totalDuration = totalDuration.plus(Duration.between(LocalTime.MIN, product.getManufactureTime()));
//        }
//        return totalDuration;
//    }

    public Duration getTotalManufacturingTime() {
        Duration totalManufacturingTime = Duration.ZERO;

        for (Product product : cartItems) {
            Duration manufacturingTime = product.getManufactureTime();
            totalManufacturingTime = totalManufacturingTime.plus(manufacturingTime);
        }

        return totalManufacturingTime;
    }


}
