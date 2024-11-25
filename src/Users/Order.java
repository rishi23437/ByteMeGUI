package Users;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Order {
    private ArrayList<FoodItem> items = new ArrayList<FoodItem>();                          // items ordered
    private ArrayList<Integer> quantities = new ArrayList<Integer>();                      // quantities for the items
    private int total_price;
    private Customer customer;                      // customer who ordered the item
    private final Instant arrival_time;                   // tracks arrival time
    protected static int total_sales = 0;
    protected static int completed_orders = 0;

    public enum Status {PENDING, COMPLETED, CANCELLED, DENIED};
    private Status status;

    protected static ArrayList<Order> cancelled_or_denied_orders = new ArrayList<>();
    private boolean refund;

    // contains only current pending orders
    protected static PriorityQueue<Order> orders = new PriorityQueue<>(new CustomerPQComparator());

    public Order(ArrayList<FoodItem> items, ArrayList<Integer> quant, Customer customer, int price) {
        this.items = items;
        this.customer = customer;
        this.quantities = quant;
        this.total_price = price;
        this.status = Status.PENDING;
        this.arrival_time = Instant.now();
        total_sales += price;
    }

    public Order(int price, Customer c, Status status) {
        // THIS IS FOR FILE HANDLING - TRANSFERRING FROM FILE TO ORDER HISTORY LIST
        this.total_price = price;
        this.customer = c;
        this.status = status;
        this.arrival_time = Instant.now();
    }


    public void print_info() {
        System.out.println("Customer: " + customer.getName() + "\n");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(items.get(i).toString());
            System.out.println("Quantity: " + quantities.get(i));
            System.out.println();
        }
        System.out.println("Total Price: " + total_price);
        System.out.println("Status: " + this.status);
        if (status == Status.CANCELLED && !refund) {
            System.out.println("Refund not processed yet.");
        }
        else if (status == Status.CANCELLED && refund) {
            System.out.println("Refund completed!");
        }
    }

    public void transfer_order_to_file() {
        /*
        Appends order to the Order History of the Customer in the file
        WRITE THIS USING BUFFERED WRITER. For each order, these are the lines:
        1. "+ total_price status"
        2. Fooditem wale lines: "= name category price quantity special_request"
        ADD o TO FILE(identify customer using name, scroll to line just before next customer, add.
        ADMIN IS CALLING THIS AFTER processing orders and status pending se hatake
        */

        String data_file_path = "C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\customersData.txt";
        String temp_file_path = "C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\temp.txt";

        try(BufferedReader file_reader = new BufferedReader(new FileReader(data_file_path));
            BufferedWriter file_writer = new BufferedWriter(new FileWriter(temp_file_path)))
        {
            String line;
            while ((line = file_reader.readLine()) != null) {
                // write each line
                file_writer.write(line);
                file_writer.newLine();

                // Identifying the line containing the required customer(who had ordered this order)
                if (line.startsWith("- ") && line.substring(2).strip().equals(this.getCustomer().getName())) {
                    // writing all the other data of the customer
                    while ((line = file_reader.readLine()) != null && !line.startsWith("- ")) {
                        file_writer.write(line);
                        file_writer.newLine();
                    }

                    // appending the order details
                    file_writer.write("+ " + this.total_price + " " + this.status.toString());
                    file_writer.newLine();

                    // adding the fooditems for the order
                    for (int num = 0; num < this.quantities.size(); num++) {
                        FoodItem item = this.items.get(num);
                        int quantity = this.quantities.get(num);
                        file_writer.write("= " + item.getName() + " " + item.getCategory() + " " +
                                item.getPrice() + " " + quantity + " " + item.getSpecial_request());
                        file_writer.newLine();
                    }

                    // writing the first line of the next customer(currently, the line variable contains that line
                    if (line != null) {
                        file_writer.write(line);
                        file_writer.newLine();
                    }
                    break;
                }
            }
            while ((line = file_reader.readLine()) != null) {
                file_writer.write(line);
                file_writer.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Path temp_file = Paths.get(temp_file_path);
            Path data_file = Paths.get(data_file_path);
            Files.move(temp_file, data_file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add_item_and_quantity(FoodItem item, int quantity) {
        items.add(item);
        quantities.add(quantity);
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        // Also use this to CHANGE STATUS TO DENIED FROM PENDING, when ADMIN REMOVES AN ITEM present in the order
        this.status = status;
        if (status == Status.CANCELLED || status == Status.DENIED) {
            total_sales -= this.total_price;
            cancelled_or_denied_orders.add(this);
            refund = false;

            for (int i = 0; i < items.size(); i++) {
                for (FoodItem menu_item: FoodItem.menu) {
                    if (items.get(i).getName().equals(menu_item.getName())) {
                        // Resetting <bought>
                        menu_item.bought -= quantities.get(i);
                    }
                }
            }
        }
        if (status == Status.COMPLETED) {
            completed_orders++;
        }
    }

    public Customer getCustomer() { return customer; }
    public Instant getArrival_time() { return arrival_time; }
    public ArrayList<FoodItem> getItems() { return items; }
    public void setRefund(boolean refund) { this.refund = refund; }
    public ArrayList<Integer> getQuantities() { return quantities; }
    public int getTotal_price() { return total_price; }
}
