import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static int orderIdCounter = 1;
    private static CustomerInfo customerInfo = null;
    private static boolean customerInfoUpdated = false;

    public static void main(String[] args) {
        // Create objects for the classes
        FileHandler fileHandler = new FileHandler();

        // StartPosition is set to 1 to skip the description line (first line) of the CSV file.
        WeekOpeningHours weekOpeningHours = fileHandler.readOpeningHoursFromCSV("src\\CSV\\PhotoShop_OpeningHours.csv", ";", 1);
        ProductCatalog productCatalog = fileHandler.readProductCatalogFromCSV("src\\CSV\\PhotoShop_PriceList.csv", ";", 1);

        // askUserToUseLastCart();
        // TODO!

        // Check if the shopping cart file exists
        boolean shoppingCartExists = doesShoppingCartFileExist();
        ShoppingCart shoppingCart;

        if (shoppingCartExists) {
            try {
                shoppingCart = JsonHelper.readShoppingCartFromJSON("src\\JSON\\shopping_cart.json");
                System.out.println("Previous shopping cart loaded.");
            } catch (IOException e) {
                System.out.println("Error loading shopping cart from JSON: " + e.getMessage());
                shoppingCart = new ShoppingCart(); // Create a new empty shopping cart in case of an error
            }
        } else {
            shoppingCart = new ShoppingCart();
            System.out.println("New shopping cart created.");
        }

        ZoneId zoneId = ZoneId.of("Europe/Amsterdam");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to PhotoShop!");

        while (true) {
            System.out.println("\n1. Add Item To Shopping Cart");
            System.out.println("2. Display Shopping Cart");
            System.out.println("3. Remove Item From Shopping Cart");
            System.out.println("4. Modify Customer Information");
            System.out.println("5. Create Invoice");
            System.out.println("6. Exit PhotoShop Application");
            System.out.println("Enter Your Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Display the product catalog
                    System.out.println("Available products:");
                    for (Product product : productCatalog.getProductsList()) {
                        System.out.println(product.getId() + ". " + product.getName() + " - $" + product.getPrice());
                    }

                    // Added functionality to go back without having to select any product
                    System.out.println("Enter the product ID or name to add to the shopping cart (or enter 'back' to return to the main menu): ");
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("back")) {
                        // User wants to go back to the main menu
                        break;
                    }

                    // If user does not choose "back" option, this executes
                    try {
                        int productId = Integer.parseInt(input);
                        Product selectedProduct = productCatalog.findProduct(productId);

                        if (selectedProduct != null) {
                            shoppingCart.addProduct(selectedProduct);
                            System.out.println("Product added to the shopping cart.");
                        } else {
                            System.out.println("Product not found. Please enter a valid product ID or name.");
                        }
                    } catch (NumberFormatException e) {
                        // If the input is not a valid integer (i.e., product ID), try searching by name
                        Product selectedProduct = productCatalog.findProduct(input);

                        if (selectedProduct != null) {
                            shoppingCart.addProduct(selectedProduct);
                            System.out.println("Product added to the shopping cart.");
                        } else {
                            System.out.println("Product not found. Please enter a valid product ID or name.");
                        }
                    }

                    break;

                case 2:
                    // Display current shopping cart
                    System.out.println("Current Shopping Cart:");
                    List<Product> cartItems = shoppingCart.getItems();
                    for (Product product : cartItems) {
                        System.out.println(product.getId() + ". " + product.getName() + " - $" + product.getPrice());
                    }
                    break;

                case 3:
                    // Remove item from shopping cart
                    System.out.print("Enter the product ID to remove from the shopping cart: ");
                    int productIdToRemove = scanner.nextInt();
                    boolean removed = shoppingCart.removeProduct(productIdToRemove);

                    if (removed) {
                        System.out.println("Product removed from the shopping cart.");
                    } else {
                        System.out.println("Product not found in the shopping cart.");
                    }
                    break;

                case 4:
                    // Modify customer information for the order
                    modifyCustomer();
                    customerInfoUpdated = true;
//                    System.out.print("Enter customer's full name: ");
//                    String fullName = scanner.nextLine();
//                    System.out.print("Enter customer's address: ");
//                    String address = scanner.nextLine();
//                    String invoiceId = generateInvoiceId();
//
//                    // Create CustomerInfo object
//                    customerInfo = new CustomerInfo(fullName, address, Instant.now(), invoiceId);
//                    System.out.println("Customer information updated.");
//
//                    customerInfoUpdated = true;
//
//                    break;
                case 5:
                    // Generate invoice and display it
                    if (!customerInfoUpdated) {
                        System.out.println("Please update customer information first.");
                        modifyCustomer();
                    }
                    Instant validPickupInstant = weekOpeningHours.calculateDeliveryDate(
                            Instant.now(),
                            shoppingCart.getTotalManufacturingTime(),
                            zoneId
                    );

                    if (validPickupInstant != null) {
                        Order order = new Order(
                                orderIdCounter++,
                                customerInfo.getFullName(),
                                shoppingCart.getItems(),
                                Instant.now(),
                                validPickupInstant
                        );

                        // Create Invoice with CustomerInfo
                        Invoice invoice = new Invoice(order, weekOpeningHours, validPickupInstant, customerInfo);

                        // Print the invoice
                        System.out.println(invoice);
                    } else {
                        System.out.println("Cannot calculate valid pickup time. Shopping cart is empty.");
                    }
                    break;







                case 6:
                    // Exit the program
                    System.out.println("Exiting PhotoShop. Thank you!");
                    scanner.close();
//                    try {
//                        JsonHelper.saveToJson(shoppingCart, "src\\JSON\\shopping_cart.json");
//                        System.out.println("Shopping cart saved to JSON.");
//                    } catch (IOException e) {
//                        System.out.println("Error saving shopping cart to JSON: " + e.getMessage());
//                    }
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Boolean to check if JSON file of previous cart exists
    private static boolean doesShoppingCartFileExist() {
        File file = new File("shopping_cart.json");
        return file.exists();
    }
    // Boolean to check if user wants to keep the cart of last session
    private static boolean askUserToUseLastCart() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to use the last used shopping cart? (Y/N): ");
        String userInput = scanner.nextLine().trim().toUpperCase();
        return userInput.equals("Y");
    }

    private static CustomerInfo modifyCustomerInformation(Scanner scanner, CustomerInfo customerInfo) {
        System.out.println("Modify Customer Information:");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        customerInfo.setFullName(fullName);

        System.out.print("Address: ");
        String address = scanner.nextLine();
        customerInfo.setAddress(address);

        // You can add similar prompts for other customer information fields

        return customerInfo;
    }

    private static String generateInvoiceId() {
        String format = "INV%03d"; // Format for invoice ID with leading zeros
        String invoiceId;

        do {
            invoiceId = String.format(format, orderIdCounter++);
        } while (isInvoiceIdTaken(invoiceId)); // Check if the generated ID is already taken

        return invoiceId;
    }

    private static boolean isInvoiceIdTaken(String invoiceId) {
        // Assuming you have a HashSet to store used invoice IDs
        Set<String> usedInvoiceIds = new HashSet<>(); // Initialize this HashSet with existing invoice IDs

        // Check if the given invoice ID is already in the set
        return usedInvoiceIds.contains(invoiceId);
    }

    public static void modifyCustomer(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer's full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter customer's address: ");
        String address = scanner.nextLine();
        String invoiceId = generateInvoiceId();

        // Create CustomerInfo object
        customerInfo = new CustomerInfo(fullName, address, Instant.now(), invoiceId);
        System.out.println("Customer information updated.");

    }

}
