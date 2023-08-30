import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    private List<Product> products = new ArrayList<>();

    // Used to populate the ProductCatalog
    public void addProduct(Product product) {
        products.add(product);
    }

    // Methods to find product based on Id or ProductName
    public Product findProduct(Object searchParam) {
        for (Product product : products) {
            if (searchParam instanceof String && product.getName().equalsIgnoreCase((String) searchParam)) {
                return product;
            } else if (searchParam instanceof Integer && product.getId() == (int) searchParam) {
                return product;
            }
        }
        return null;
    }

    // Method to retrieve the populated Productlist
    public List<Product> getProductsList() {
        return products;
    }

    // Method to display the Product Catalog for the consumer to choose items for their order
    public void displayProductCatalog() {
        System.out.println("Product Catalog:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

}
