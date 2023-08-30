import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    // Abstract method to read the CSV files:
    public List<List<String>> getCSVRecords(String filePath, String delimiter) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            List<List<String>> records = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                // Split the CSV line into separate values with given delimiter
                List<String> values = List.of(line.split(delimiter));
                records.add(values);
            }
            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Methods to write the data from the CSV file to an object
    public WeekOpeningHours readOpeningHoursFromCSV(String filePath, String delimiter, int startPosition) {
        WeekOpeningHours weekOpeningHours = new WeekOpeningHours();
        List<List<String>> records = getCSVRecords(filePath, delimiter);

        for (int i = startPosition; i < records.size(); i++) {
            // Assuming the structure of the CSV is: Day Number, Day Name, Opening Time, Closing Time

            List<String> row = records.get(i);

            if (row.size() == 4) {
                int dayNumber = Integer.parseInt(row.get(0).trim());
                String dayName = row.get(1).trim();

                // Parse the time strings (Opening Time and Closing Time)
                LocalTime openFrom = LocalTime.parse(row.get(2).trim());
                LocalTime openUntil = LocalTime.parse(row.get(3).trim());

                // Create a new OpeningHours object and add it to the list
                OpeningHours workDay = new OpeningHours(dayNumber, dayName, openFrom, openUntil);
                weekOpeningHours.addOpeningHours(workDay);
            }
        }
        // Return the populated OpeningHours object
        return weekOpeningHours;
    }

    public ProductCatalog readProductCatalogFromCSV(String filePath, String delimiter, int startPosition) {
        ProductCatalog productCatalog = new ProductCatalog();
        List<List<String>> records = getCSVRecords(filePath, delimiter);

        for (int i = startPosition; i < records.size(); i++) {
            // Assuming the structure of the CSV is: Product ID, Product Name, Product Price, Manufacturing Time
            List<String> row = records.get(i);

            if (row.size() == 4) {
                int id = Integer.parseInt(row.get(0).trim());
                String name = row.get(1).trim();
                double price = Double.parseDouble(row.get(2).trim());

                // Parse the manufacturing time as a string in the format "HH:mm" and convert it to a Duration
                String manufacturingTimeString = row.get(3).trim();
                String[] timeParts = manufacturingTimeString.split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                long totalSeconds = hours * 3600L + minutes * 60L;
                Duration manufactureTime = Duration.ofSeconds(totalSeconds);

                // Create a new Product object and add it to the list
                Product product = new Product(id, name, price, manufactureTime);
                productCatalog.addProduct(product);
            }
        }
        // Return the populated ProductCatalog object
        return productCatalog;
    }


}