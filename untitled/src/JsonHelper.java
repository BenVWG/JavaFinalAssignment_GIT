import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonHelper {
    private static final Gson gson = new Gson();

    public static void saveToJson(ShoppingCart shoppingCart, String filePath) throws IOException {
        String json = gson.toJson(shoppingCart);
        Files.write(Paths.get(filePath), json.getBytes());
    }

    public static ShoppingCart loadFromJson(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return gson.fromJson(json, ShoppingCart.class);
    }

    public static ShoppingCart readShoppingCartFromJSON(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return gson.fromJson(json, ShoppingCart.class);
    }
}
