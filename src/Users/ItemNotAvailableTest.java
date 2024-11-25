package Users;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


public class ItemNotAvailableTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final InputStream inp = System.in;
    private final PrintStream outp = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(outp);
        System.setIn(inp);
    }

    @Test
    void test_ordering_unavailable_item() {
        // adding an out-of-stock item
        FoodItem unavailableItem = new FoodItem("PIZZA", "MEAL", 450);
        unavailableItem.setAvailable(false);                                     // marking item as not available
        FoodItem.menu.add(unavailableItem);

        // ordering out-of-stock item(pizza)
        String required_input = "PIZZA";
        InputStream inputStream = new ByteArrayInputStream(required_input.getBytes());
        System.setIn(inputStream);

        Customer customer = new Customer("test_customer", "test_password");
        customer.test_add_item();

        // testing using assertTrue
        String output = outputStream.toString();
        assertTrue(output.contains("Item with given name not found or not available."),
                "Expected error message for out-of-stock item.");
    }
}
