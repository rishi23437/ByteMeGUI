package Users;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.nio.file.*;


public class Customer extends User {
    private boolean vip = false;
    private boolean current_pending_order = false;

    // LIFO, therefore sorted based on jo order last khatam hua
    private Stack<Order> order_history = new Stack<Order>();

    public Customer() {
        this.password = null;
    }

    public Customer(String name, String password) {
        super(name);
        this.password = password;
    }

    public Customer(String name, String password, boolean vip) {
        super(name);
        this.password = password;
        this.vip = vip;
    }

    @Override
    public void print_functionalities() {
        System.out.println("Operations available to perform:");
        System.out.println("0: Quit");
        System.out.println("1: Become a VIP");
        System.out.println("2: View Menu");
        System.out.println("3: Search for an item");
        System.out.println("4: Filter items based on categories");
        System.out.println("5: Sort items based on their prices(ascending/descending order)");
        System.out.println("6: Place an order");
        System.out.println("7: View Order Status");
        System.out.println("8: Cancel Order");
        System.out.println("9: View Order History");
        System.out.println("10: Reorder a previously ordered set of items");
    }

    public void add_order_to_history(Order order) {
        order_history.push(order);
    }

    public Order get_last_order() {
        return order_history.peek();
    }

    // 1. BECOME VIP
    public void become_vip() {
        if (this.vip) {
            System.out.println("You are already a VIP.");
            return;
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("You have to pay Rs.1000. Do you want to continue? (Y/N)");
        String choice = scan.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            this.vip = true;

            // UPDATING IN FILE. Write everything to temp.txt, transfer temp.txt to customersDATA.txt
            try (BufferedReader file_reader = new BufferedReader(new FileReader("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\customersData.txt"));
                 BufferedWriter file_writer = new BufferedWriter(new FileWriter("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\temp.txt")))
            {
                String line;
                while ((line = file_reader.readLine()) != null) {
                    // Identifying this customer in the file
                    if (line.startsWith("- ") && line.substring(2).strip().equals(this.name)) {
                        file_writer.write(line);
                        file_writer.newLine();

                        String password = file_reader.readLine();
                        file_writer.write(password);
                        file_writer.newLine();

                        // skip previous value of vip, write new value
                        file_reader.readLine();
                        file_writer.write(String.valueOf(this.vip));
                    }
                    else {
                        // All other lines
                        file_writer.write(line);
                        file_writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // temp.txt -> customersData.txt
            try {
                Path temp_file = Paths.get("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\temp.txt");
                Path data_file = Paths.get("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\customersData.txt");
                Files.move(temp_file, data_file, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("You are now a VIP!");
        }
    }

    // BROWSE MENU
    // 2. View All items
    public void print_menu() {
        System.out.println("-----------------------MENU---------------------:");
        for (FoodItem item: FoodItem.menu) {
            System.out.println(item);
            System.out.println();
        }
    }

    // 3. Search Functionality
    public void search() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the name of the item you would like to search:");
        String name = scan.nextLine().toUpperCase();
        for (FoodItem item: FoodItem.menu) {
            if (name.equals(item.getName())) {
                System.out.println(item);
                return;
            }
        }
        System.out.println("Item with given name not found.");
    }

    // 4. Filter by Category
    public void filter() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the category of items which you would like to search:");
        String category = scan.nextLine().toUpperCase();

        boolean found = false;
        for (FoodItem item: FoodItem.menu) {
            if (category.equals(item.getCategory())) {
                System.out.println(item);
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No item with the given category exists in the menu.");
        }
    }

    // 5. Sort Menu by price
    public void sort_menu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Do you want to sort the items in ascending or descending order? (A/D):");
        String choice = scan.nextLine().toUpperCase();

        FoodItem.menu.sort(new MenuPriceComparator());
        if (choice.equalsIgnoreCase("a")) {
            // Complete this, and descending order wala case
            System.out.println("Food Items sorted by prices in ascending order:");
            for (FoodItem item: FoodItem.menu) {
                System.out.println(item);
                System.out.println();
            }
        }
        else {
            System.out.println("Food Items sorted by prices in descending order:");
            for (int i = FoodItem.menu.size() - 1; i >= 0; i--) {
                System.out.println(FoodItem.menu.get(i));
                System.out.println();
            }
        }
    }

    // CART OPERATIONS
    public void cart_operations() {
        System.out.println("1: Add an item");
        System.out.println("2: Modify the quantity of an existing item");
        System.out.println("3: Remove an item");
        System.out.println("4: View the total amount to be paid");
        System.out.println("5: PLACE THE ORDER");
    }

    // 6. Place an order
    public void place_order() {
        Scanner scan = new Scanner(System.in);
        ArrayList<FoodItem> items = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        int total_price = 0;

        while (true) {
            this.cart_operations();
            System.out.println("What would you like to do?");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                continue;
            }
            int op = scan.nextInt();
            scan.nextLine();

            if (op == 1) {
                // ADD AN ITEM
                System.out.println("Enter the name of the item you would like to add:");
                String name = scan.nextLine().toUpperCase();
                boolean found = false;
                for (FoodItem item: FoodItem.menu) {                            // Searching for item
                    if (name.equals(item.getName()) && item.isAvailable()) {
                        // ITEM FOUND
                        System.out.println("Item has been found!");
                        System.out.println("Enter the quantity of this item:");
                        if (!scan.hasNextInt()) {                                   // check for int
                            scan.nextLine();
                            System.out.println("Please enter a number!");
                            return;
                        }
                        int quant = scan.nextInt();
                        scan.nextLine();
                        while (quant <= 0) {
                            System.out.println("Price is non-positive. Enter a positive value for price.");
                            if (!scan.hasNextInt()) {                                   // check for int
                                scan.nextLine();
                                System.out.println("Please enter a number!");
                                continue;
                            }
                            quant = scan.nextInt();
                            scan.nextLine();
                        }
                        // item and its quantity have been decided
                        System.out.println("Do you want to add a special request for this item? (Y/N)");
                        String ch = scan.nextLine();
                        String request = "";
                        if (ch.equalsIgnoreCase("y")) {
                            System.out.println("Enter your request: ");
                            request = scan.nextLine();
                        }
                        // CREATING NEW FOOD_ITEM OBJECT SO THAT CHANGES DONT REFLECT IN THE OBJECT PRESENT IN THE MENU
                        FoodItem new_item = new FoodItem(item.getName(), item.getCategory(), item.getPrice(), request);
                        items.add(new_item);
                        quantities.add(quant);
                        total_price += (quant)*(item.getPrice());
                        System.out.println("The item has been added to your cart.");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Item with given name not found or not available.");
                }
            }
            else if (op == 2) {
                // MODIFY QUANTITIES
                System.out.println("Enter the name of the item whose quantity you would like to modify:");
                String name = scan.nextLine().toUpperCase();
                boolean found = false;
                for (int i = 0; i < items.size(); i++) {
                    if (name.equals(items.get(i).getName())) {
                        System.out.println("Item has been found!");
                        total_price -= (items.get(i).getPrice())*quantities.get(i);

                        System.out.println("Enter the new quantity of this item:");
                        if (!scan.hasNextInt()) {                                   // check for int
                            scan.nextLine();
                            System.out.println("Please enter a number!");
                            return;
                        }
                        int quant = scan.nextInt();
                        scan.nextLine();
                        while (quant <= 0) {
                            System.out.println("Price is non-positive. Enter a positive value for price.");
                            if (!scan.hasNextInt()) {                                   // check for int
                                scan.nextLine();
                                System.out.println("Please enter a number!");
                                continue;
                            }
                            quant = scan.nextInt();
                            scan.nextLine();
                        }
                        total_price += (quant)*(items.get(i).getPrice());               // updating total price
                        quantities.set(i, quant);                                       // replacing previous value of quantity with quant
                        System.out.println("The quantity of the item has been modified successfully.");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Item with given name not found.");
                }
            }
            else if (op == 3) {
                System.out.println("Enter the name of the item you would like to remove:");
                String name = scan.nextLine().toUpperCase();
                boolean found = false;
                for (int i = 0; i < items.size(); i++) {
                    if (name.equals(items.get(i).getName())) {
                        // ITEM FOUND
                        System.out.println("Item has been found!");
                        total_price -= (items.get(i).getPrice())*quantities.get(i);
                        items.remove(i);
                        quantities.remove(i);
                        System.out.println("The item has been removed successfully!");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Item with given name not found.");
                }
            }
            else if (op == 4) {
                System.out.println("TOTAL PRICE: " + total_price);
            }
            else if (op == 5) {
                System.out.println("Enter your address where you want your food to be delivered.");
                String address = scan.nextLine();
                System.out.println("Do you want to place your order and pay? (Y/N)");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("y")) {
                    Order new_order = new Order(items, quantities, this, total_price);

                    // Update bought for the items in the menu
                    for (int i = 0; i < items.size(); i++) {
                        for (FoodItem menu_item: FoodItem.menu) {
                            if (items.get(i).getName().equals(menu_item.getName())) {
                                // Same Item in menu found
                                menu_item.bought += quantities.get(i);                  // incrementing bought
                            }
                        }
                    }

                    Order.orders.add(new_order);
                    System.out.println("Payment successful! Order placed successfully!");
                    this.current_pending_order = true;
                    break;
                }
            }
            else {
                System.out.println("Enter a valid choice.");
            }
        }
    }

    // 7. View order status
    public void view_order_status() {
        if (this.isCurrent_pending_order()) {
            System.out.println("Your order is pending.\n");
            for (Order o: Order.orders) {
                if (o.getCustomer().equals(this)) {
                    o.print_info();
                    break;
                }
            }
        }
        else {
            System.out.println("No pending order found. If you had placed an order, then please check your Order History, " +
                    "it may have been Completed, Cancelled, or Denied.");
        }
    }

    // 8. Cancel Order
    public void cancel_order() {
        if (this.isCurrent_pending_order()) {
            for (Order o: Order.orders) {
                if (o.getCustomer().equals(this)) {
                    o.setStatus(Order.Status.CANCELLED);                // Order added to Cancelled orders list(see setStatus method of Order class)
                    this.order_history.push(o);
                    Order.orders.remove(o);
                    this.current_pending_order = false;
                    System.out.println("Order cancelled successfully. You will get your refund in due time.");
                    return;
                }
            }
        }
        else {
            System.out.println("You have no pending orders.");
        }
    }

    // 9. View Order History
    public boolean view_order_history() {
        int order_num = 1;
        if (order_history.isEmpty()) {
            System.out.println("You have not ordered anything yet, or your previous order is pending.");
            return false;
        }
        for (Order o: order_history) {
            System.out.println("Order " + order_num + ": ----------------------------------");
            o.print_info();
            order_num++;
            System.out.println();
        }
        return true;
    }

    // 10. Reorder a previous meal
    public void reorder() {
        Scanner scan = new Scanner(System.in);
        boolean exists = view_order_history();
        if (!exists) {
            System.out.println("You can't reorder anything yet, since your order history is empty.");
            return;
        }
        System.out.println("Enter the order number which you would like to reorder: ");
        if (!scan.hasNextInt()) {                                   // check for int
            scan.nextLine();
            System.out.println("Please enter a number!");
            return;
        }
        int order_num = scan.nextInt();
        scan.nextLine();
        while (order_num < 1 || order_num > order_history.size()) {
            System.out.println("Order number is either not positive or greater than the total number of orders.");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                continue;
            }
            order_num = scan.nextInt();
            scan.nextLine();
        }
        Order prev_order = order_history.get(order_num - 1);
        Order new_order = new Order(prev_order.getItems(), prev_order.getQuantities(), this, prev_order.getTotal_price());
        Order.orders.add(new_order);
        System.out.println("Payment successful! Order placed successfully!");
        this.current_pending_order = true;
    }


    public String getName() {
        return this.name;
    }
    public String getPassword() {
        return this.password;
    }

    public Boolean isVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public Stack<Order> getOrder_history() {
        return order_history;
    }

    public void setCurrent_pending_order(boolean current_pending_order) {
        this.current_pending_order = current_pending_order;
    }

    public boolean isCurrent_pending_order() {
        return current_pending_order;
    }
}
