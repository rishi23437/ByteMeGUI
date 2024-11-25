package Users;

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Admin extends User {
    /*
    1. Password for all admins is "a"
    */

    /*
    MANAGING ORDERS:
    Priorities -
    1. VIP Customer
    2. First in First Out
    */

    public Admin(String name) {
        super(name);
        this.password = "a";
    }

    @Override
    public void print_functionalities() {
        System.out.println("Operations available to perform:");
        System.out.println("0: Quit");
        System.out.println("1: Add Food Item");
        System.out.println("2: Update Food Item");
        System.out.println("3: Remove Food Item");
        System.out.println("4: View Pending orders");
        System.out.println("5: Process Orders");
        System.out.println("6: Process Refund for Cancelled Orders");
        System.out.println("7: End sales for the day");
        System.out.println("8: View the Daily Sales Report");
        System.out.println("9: View the Menu page of the GUI");
        System.out.println("10: View the Pending Orders page of the GUI");
    }


    // MENU MANAGEMENT
    // 1. Add item
    public void add_item() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the item");
        String name = scan.nextLine().toUpperCase();

        for (FoodItem item : FoodItem.menu) {
            if (name.equals(item.getName())) {
                System.out.println("Item already exists. Choose a different name or perform a different operation.");
                return;
            }
        }

        System.out.println("Enter the category of the item");
        String category = scan.nextLine().toUpperCase();

        System.out.println("Enter the price of the item");
        if (!scan.hasNextInt()) {                                   // check for int
            scan.nextLine();
            System.out.println("Please enter a number!");
            return;
        }
        int price = scan.nextInt();
        scan.nextLine();
        while (price <= 0) {
            System.out.println("Price is non-positive. Enter a positive value for price.");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                continue;
            }
            price = scan.nextInt();
            scan.nextLine();
        }
        FoodItem item = new FoodItem(name, category, price);
        FoodItem.menu.add(item);

        // Adding item to Menu file
        try (BufferedWriter file_writer = new BufferedWriter(new FileWriter("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\menu.txt", true)))
        {
            file_writer.newLine();
            file_writer.write("- " + name);
            file_writer.newLine();
            file_writer.write(category);
            file_writer.newLine();
            file_writer.write(String.valueOf(price));
            file_writer.newLine();
            file_writer.write(String.valueOf(true));
            file_writer.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Item added successfully.");
    }

    // 2. Update Item Details(Price and/or Availability)
    public void update_item() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the item");
        String name = scan.nextLine();

        // Searching for item
        FoodItem required_item = null;
        for (FoodItem item : FoodItem.menu) {
            if (name.toUpperCase().equals(item.getName())) {
                required_item = item;
                System.out.println(item);                       // uses toString of FoodItem
            }
        }
        if (required_item == null) {
            System.out.println("Item with given name not found.");
            return;
        }

        String menu_file_path = "C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\menu.txt";
        String temp_file_path = "C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\temp.txt";

        boolean done = false;
        // Changing availability if required
        System.out.println("Do you want to change the Availability of the item? Enter Y/N");
        String choice = scan.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            done = true;
            System.out.println("Should the item be available or not? 1: available, any other number: not available");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                return;
            }
            int c = scan.nextInt();
            scan.nextLine();
            boolean b = c == 1;                     // if c: 1 -> b = true, else b -> false ie., short if statement
            required_item.setAvailable(b);

            // CHANGE AVAILABILITY IN FILE
            try (BufferedReader file_reader = new BufferedReader(new FileReader(menu_file_path));
                 BufferedWriter file_writer = new BufferedWriter(new FileWriter(temp_file_path)))
            {
                String line;
                while ((line = file_reader.readLine()) != null) {
                    file_writer.write(line);
                    file_writer.newLine();

                    // identifying line containing name of required item
                    if (line.startsWith("- ") && line.substring(2).strip().equals(required_item.getName())) {
                        // writing category and price
                        String category = file_reader.readLine();
                        file_writer.write(category);
                        file_writer.newLine();

                        String price = file_reader.readLine();
                        file_writer.write(price);
                        file_writer.newLine();

                        // updating availability
                        file_reader.readLine();                         // skipping old availability line
                        file_writer.write(String.valueOf(b));
                        file_writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Path temp_path = Paths.get(temp_file_path);
                Path menu_path = Paths.get(menu_file_path);
                Files.move(temp_path, menu_path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Availability updated successfully.");
        }

        // Changing price if required
        System.out.println("Do you want to change the Price of the item? Enter Y/N");
        choice = scan.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            done =  true;
            System.out.println("Enter the new price of the item");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                return;
            }
            int price = scan.nextInt();
            scan.nextLine();
            while (price <= 0) {
                System.out.println("Price is non-positive. Enter a positive value for price.");
                if (!scan.hasNextInt()) {                                   // check for int
                    scan.nextLine();
                    System.out.println("Please enter a number!");
                    continue;
                }
                price = scan.nextInt();
                scan.nextLine();
            }
            required_item.setPrice(price);

            // CHANGE PRICE IN FILE
            try (BufferedReader file_reader = new BufferedReader(new FileReader(menu_file_path));
                 BufferedWriter file_writer = new BufferedWriter(new FileWriter(temp_file_path)))
            {
                String line;
                while ((line = file_reader.readLine()) != null) {
                    file_writer.write(line);
                    file_writer.newLine();

                    // identifying line containing name of required item
                    if (line.startsWith("- ") && line.substring(2).strip().equals(required_item.getName())) {
                        String category = file_reader.readLine();
                        file_writer.write(category);
                        file_writer.newLine();

                        file_reader.readLine();                         // skipping old price
                        file_writer.write(String.valueOf(price));                       // writing new price
                        file_writer.newLine();

                        String availability = file_reader.readLine();
                        file_writer.write(availability);
                        file_writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Path temp_path = Paths.get(temp_file_path);
                Path menu_path = Paths.get(menu_file_path);
                Files.move(temp_path, menu_path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Price updated successfully.");
        }
        if (!done) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }
        System.out.println("Item details updated successfully.");
    }

    // 3. Remove item
    public void remove_item() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the item to be removed");
        String name = scan.nextLine().toUpperCase();

        for (FoodItem item: FoodItem.menu) {
            if (name.equals(item.getName())) {
                System.out.println("The required item has been found.");
                System.out.println(item);

                // Changing status of all pending orders containing the item to "denied"
                Iterator<Order> it = Order.orders.iterator();
                while (it.hasNext()) {
                    Order order = it.next();
                    for (FoodItem food: order.getItems()) {
                        if (food.getName().equals(item.getName())) {
                            order.setStatus(Order.Status.DENIED);
                            order.remove_order_from_pendingOrders_file();
                        }
                    }
                }

                FoodItem.menu.remove(item);

                // REMOVE ITEM FROM FILE
                String menu_file_path = "C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\menu.txt";
                String temp_file_path = "C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\temp.txt";

                try (BufferedReader file_reader = new BufferedReader(new FileReader(menu_file_path));
                     BufferedWriter file_writer = new BufferedWriter(new FileWriter(temp_file_path)))
                {
                    String line;
                    while ((line = file_reader.readLine()) != null) {
                        // identifying line containing name of required item
                        if (line.startsWith("- ") && line.substring(2).strip().equals(item.getName())) {
                            // skipping category, price and availability
                            file_reader.readLine();
                            file_reader.readLine();
                            file_reader.readLine();
                            continue;
                        }
                        // write all other lines
                        file_writer.write(line);
                        file_writer.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Path temp_path = Paths.get(temp_file_path);
                    Path menu_path = Paths.get(menu_file_path);
                    Files.move(temp_path, menu_path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Item removed successfully.");
                return;
            }
        }
        System.out.println("Item with the given name not found. Please try again.");
    }

    // ORDER MANAGEMENT
    // 4. View pending orders
    public void view_orders() {
        // ASSUMING orders ONLY HAS PENDING ORDERS
        System.out.println("-------------------------ORDERS-------------------------");
        Iterator<Order> it = Order.orders.iterator();
        int index = 1;
        while (it.hasNext()) {
            Order order = it.next();
            System.out.println("Order " + index + ": ---------------------------------\n");
            order.print_info();
            System.out.println();
            index++;
        }
    }

    // Update order status. Status will be updated(order will be processed) only by Admin(process_orders()), except when customer cancels order.
    public boolean update_order_status(Order o) {
        Scanner scan = new Scanner(System.in);
        o.print_info();
        System.out.println("Enter the new status for the order:");
        String new_status = scan.nextLine().toUpperCase();
        if (new_status.equalsIgnoreCase(Order.Status.COMPLETED.toString())) {
            o.setStatus(Order.Status.COMPLETED);
        }
        else if (new_status.equalsIgnoreCase(Order.Status.CANCELLED.toString())) {
            o.setStatus(Order.Status.CANCELLED);
        }
        else if (new_status.equalsIgnoreCase(Order.Status.DENIED.toString())) {
            o.setStatus(Order.Status.DENIED);
        }
        else {
            System.out.println("Entered status is invalid or already set.");
            return false;
        }
        System.out.println("Order status updated successfully.");
        o.getCustomer().setCurrent_pending_order(false);                        // order not pending now
        o.getCustomer().getOrder_history().push(o);
        o.transfer_order_to_customers_file();
        o.remove_order_from_pendingOrders_file();
        Order.orders.poll();
        return true;
    }

    // 5. Process orders(finish them, change their status)
    public void process_orders() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Total number of orders pending: " + Order.orders.size());
        System.out.println("How many orders do you want to process?");
        if (!scan.hasNextInt()) {                                   // check for int
            scan.nextLine();
            System.out.println("Please enter a number!");
            return;
        }
        int num_of_orders = scan.nextInt();
        scan.nextLine();
        while (num_of_orders < 0 || num_of_orders > Order.orders.size()) {
            System.out.println("Number of orders is either not positive or greater than the total number of orders.");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                continue;
            }
            num_of_orders = scan.nextInt();
            scan.nextLine();
        }
        if (num_of_orders == 0) {
            System.out.println("No orders processed");
            return;
        }

        // Now number of orders is positive
        int count = 0;
        while (count < num_of_orders) {
            Iterator<Order> it = Order.orders.iterator();
            Order order = it.next();
            boolean success = update_order_status(order);
            if (!success) {
                System.out.println("Some orders were not processed because of invalid status.");
                return;
            }
            count++;
        }
    }

    // 6. Process refunds of cancelled orders
    public void process_refund() {
        Scanner scan = new Scanner(System.in);
        if (Order.cancelled_or_denied_orders.isEmpty()) {
            System.out.println("There are no cancelled or denied orders");
            return;
        }

        System.out.println("The following are the cancelled/denied orders, whose refunds have not been processed:");
        Iterator<Order> it = Order.cancelled_or_denied_orders.iterator();
        int index = 1;
        while (it.hasNext()) {
            Order order = it.next();
            System.out.println("Order " + index + ": -----------------------------\n");
            order.print_info();
            index++;
        }

        System.out.println("Enter the index of the order for which you want to process the refund: ");
        if (!scan.hasNextInt()) {                                   // check for int
            scan.nextLine();
            System.out.println("Please enter a number!");
            return;
        }
        int order_num = scan.nextInt();
        scan.nextLine();
        while (order_num < 1 || order_num > Order.cancelled_or_denied_orders.size()) {
            System.out.println("Number of orders is either not positive or greater than the total number of orders.");
            if (!scan.hasNextInt()) {                                   // check for int
                scan.nextLine();
                System.out.println("Please enter a number!");
                continue;
            }
            order_num = scan.nextInt();
            scan.nextLine();
        }

        Order.cancelled_or_denied_orders.get(order_num - 1).setRefund(true);
        Order.cancelled_or_denied_orders.remove(order_num - 1);
        System.out.println("Refund successful!");
    }

    // 7. End sales for the day
    public void next_day() {
        // Current day over, switch to next day. GENERATE DAILY REPORT BEFORE THIS
        Order.total_sales = 0;
        Order.completed_orders = 0;
        for (FoodItem item: FoodItem.menu) {
            item.setBought(0);
        }
        System.out.println("Current day is over. Please come back tomorrow!");
    }

    // 8. Generate Daily Sales Report
    public void daily_sales_report() {
        System.out.println("Total number of Completed Orders for the day: " + Order.completed_orders);
        System.out.println("Total sales for the day: " + Order.total_sales);

        // To get most popular items
        FoodItem.menu.sort(new MenuBoughtComparator());
        for (FoodItem item: FoodItem.menu) {
            System.out.println(item.getName() + " was bought " + item.bought + " number of times.");
        }
    }


    public String getName() {
        return this.name;
    }
}
