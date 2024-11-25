import Users.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class InvalidLoginAttemptsTest {

    private String login(String name, String password) {
        // NOTE: The code here is copied from the login part of the main method of Main class

        for (Customer c : Main.customers) {
            if (c.getName().equals(name) && !c.getPassword().equals(password)) {
                // Customer EXISTS, password is wrong
                return "User already exits and password is incorrect. Try again.";
            }
            else if (c.getName().equals(name) && c.getPassword().equals(password)) {
                // Customer exists, password is right(successful LOGIN)
                return "You have successfully logged in.";
            }
        }

        return "User does not exist. Please register. (in the actual code, this goes to SignUp";
    }

    @Test
    public void testInvalidLoginAttempts() {
        Main.customers.add(new Customer("rishi3", "pass123"));

        // INVALID USERNAME
        String test1 = login("random", "pass123");
        assertEquals("User does not exist. Please register. (in the actual code, this goes to SignUp", test1);

        // INVALID PASSWORD
        String test2 = login("rishi3", "something");
        assertEquals("User already exits and password is incorrect. Try again.", test2);

        // VALID LOGIN, should work
        String test3 = login("rishi3", "pass123");
        assertEquals("You have successfully logged in.", test3);
    }
}

