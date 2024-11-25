package Users;

import java.util.ArrayList;

public class FoodItem {
    /*
    1. Treat <name> as an identifier for an item. HARDCODED NAMES SHOULD BE IN UPPERCASE
    2. price should be > 0
    3. By default, an item is available
    */
    private final String name;            // NAME OF ITEM CANNOT BE CHANGED, should be UNIQUE
    private final String category;        // similar to 'domain' for Prof in Course Reg System
    private int price;                    // Taking prices of items to be integers
    private boolean available;
    private String special_request = "";
    protected int bought = 0;

    // Sort by price using self-defined function
    public static ArrayList<FoodItem> menu = new ArrayList<>();
    /*
    In MENU FILE, format is:
    - name
    category
    price
    availability
    */


    public FoodItem(String name, String category, int price) {
        // Use this constructor for copying items from menu file to menu arraylist
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = true;              // by default, item is available
    }

    public FoodItem(String name, String category, int price, String special_request) {
        // For Special requests in ORDERS of Customers, use this constructor
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = true;
        this.special_request = special_request;
    }

    // DEFINE TO STRING METHOD FOR PRINTING MENU
    @Override
    public String toString() {
        return "Name: " + name + "\nCategory: " + category + "\nPrice: " + price +
                "\nAvailable: " + available + "\nSpecial Request: " + special_request;
    }

    public String getSpecial_request() {
        return special_request;
    }

    public void setSpecial_request(String special_request) {
        this.special_request = special_request;
    }

    public String getName() { return name; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    public String getCategory() { return category; }

    public void setBought(int bought) { this.bought = bought; }
}
