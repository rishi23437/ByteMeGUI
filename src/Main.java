import Users.Admin;
import Users.Customer;
import Users.FoodItem;
import Users.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static ArrayList<Customer> customers = new ArrayList<>();

    // RIGHT NOW: DESIGN BY FLOW
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" +
                " _______             __                     __       __          __ \n" +
                "/       \\           /  |                   /  \\     /  |        /  |\n" +
                "$$$$$$$  |__    __ _$$ |_    ______        $$  \\   /$$ | ______ $$ |\n" +
                "$$ |__$$ /  |  /  / $$   |  /      \\       $$$  \\ /$$$ |/      \\$$ |\n" +
                "$$    $$<$$ |  $$ $$$$$$/  /$$$$$$  |      $$$$  /$$$$ /$$$$$$  $$ |\n" +
                "$$$$$$$  $$ |  $$ | $$ | __$$    $$ |      $$ $$ $$/$$ $$    $$ $$/ \n" +
                "$$ |__$$ $$ \\__$$ | $$ |/  $$$$$$$$/       $$ |$$$/ $$ $$$$$$$$/ __ \n" +
                "$$    $$/$$    $$ | $$  $$/$$       |      $$ | $/  $$ $$       /  |\n" +
                "$$$$$$$/  $$$$$$$ |  $$$$/  $$$$$$$/       $$/      $$/ $$$$$$$/$$/ \n" +
                "         /  \\__$$ |                                                 \n" +
                "         $$    $$/                                                  \n" +
                "          $$$$$$/                                                   ");
        System.out.println("Welcome to our online Food Ordering Application!");

        // LOADING CUSTOMER DATA FROM FILE TO LIST!!!!
        try (BufferedReader file_reader = new BufferedReader(new FileReader("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\customersData.txt")))
        {
            Customer current_customer = null;
            String line;                                            // line: name index

            // FIRST EXECUTION: DO NOT LOAD FROM FILE. first line will be empty for this case, and while loop will exit immediately
            while ((line = file_reader.readLine()) != null) {
                // '-': signifies that the data of a NEW CUSTOMER has started
                if (line.startsWith("- ")) {
                    // extracting name, password, vip
                    String name = line.substring(2).strip();
                    String password = file_reader.readLine().strip();
                    boolean vip = Boolean.parseBoolean(file_reader.readLine().strip());
                    current_customer = new Customer(name, password, vip);
                    customers.add(current_customer);
                }
                // '+': signifies starting of an ORDER. such lines: + total_price status
                else if (line.startsWith("+ ") && current_customer != null) {
                    String[] elements = line.split(" ");
                    int total_price = Integer.parseInt(elements[1]);
                    Order.Status status = Order.Status.valueOf(elements[2]);
                    Order new_order = new Order(total_price, current_customer, status);
                    current_customer.add_order_to_history(new_order);
                }
                // '=': line containing fooditem
                else if (line.startsWith("= ") && current_customer != null) {
                    String[] elements = line.split(" ");
                    String name = elements[1], category = elements[2];
                    int price = Integer.parseInt(elements[3]), quantity = Integer.parseInt(elements[4]);
                    String special_request;
                    if (elements.length > 5) {
                        // contains special request
                        special_request = String.join(" ", Arrays.copyOfRange(elements, 5, elements.length));
                    }
                    else {
                        special_request = "";
                    }
                    FoodItem new_item = new FoodItem(name, category, price, special_request);
                    current_customer.get_last_order().add_item_and_quantity(new_item, quantity);
                }
            }

            if (customers.isEmpty()) {
                System.out.println("There are no customers in the database, so most probably this is the first execution of the program!");
                System.out.println("Or no customer was registered in the previous executions.\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // LOADING MENU FROM FILE TO LIST
        try (BufferedReader file_reader = new BufferedReader(new FileReader("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\menu.txt")))
        {
            String line;
            while ((line = file_reader.readLine()) != null) {
                // Identifying new items
                if (line.startsWith("- ")) {
                    String name = line.substring(2).strip();
                    String category = file_reader.readLine().strip();
                    int price = Integer.parseInt(file_reader.readLine().strip());
                    boolean availability = Boolean.parseBoolean(file_reader.readLine().strip());

                    FoodItem item = new FoodItem(name, category, price);

                    // if availability is false, changing it to false
                    if (!availability) {
                        item.setAvailable(false);
                    }

                    FoodItem.menu.add(item);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        boolean flag = true;
        while (flag) {
            // User identification and authentication
            System.out.println("Enter your role: ");
            System.out.println("0: Quit");
            System.out.println("1: Admin");
            System.out.println("2: Customer");
            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Please enter a number!");
                continue;
            }
            int role = sc.nextInt();
            sc.nextLine();

            if (role == 0) {
                break;
            }

            else if (role == 1) {
                // Admin
                System.out.println("Enter admin password: ");
                String password = sc.nextLine();
                if (!password.equals("a")) {
                    System.out.println("Password is incorrect. Try again.");
                    continue;
                }
                System.out.println("Enter your name: ");
                String name = sc.nextLine();
                Admin admin = new Admin(name);
                System.out.println("Admin signed in Successfully.");

                // ADMIN FUNCTIONALITIES
                while (true) {
                    admin.print_functionalities();
                    System.out.print("Which operation would you like to do?");
                    if (!sc.hasNextInt()) {                                   // check for int
                        sc.nextLine();
                        System.out.println("Please enter a number!");
                        continue;
                    }
                    int op = sc.nextInt();
                    sc.nextLine();

                    // Operations
                    if (op == 0) {                                  // No operation, quit
                        System.out.println();
                        break;
                    }
                    else if (op == 1) {
                        admin.add_item();
                        System.out.println();
                    }
                    else if (op == 2) {
                        admin.update_item();
                        System.out.println();
                    }
                    else if (op == 3) {
                        admin.remove_item();
                        System.out.println();
                    }
                    else if (op == 4) {
                        admin.view_orders();
                        System.out.println();
                    }
                    else if (op == 5) {
                        admin.process_orders();
                        System.out.println();
                    }
                    else if (op == 6) {
                        admin.process_refund();
                        System.out.println();
                    }
                    else if (op == 7) {
                        admin.next_day();
                        System.out.println();
                    }
                    else if (op == 8) {
                        admin.daily_sales_report();
                        System.out.println();
                    }
                    else {
                        System.out.println("Enter a valid choice.");
                    }
                }
            }
            else if (role == 2) {
                // Customer
                // LOGIN/SIGNUP
                System.out.println("Enter your name/username: ");
                String name = sc.nextLine();
                System.out.println("Enter your password: ");
                String password = sc.nextLine();

                boolean flag1 = false;                                // for existence of customer with given name and pass
                boolean retry = false;                                // name correct, pass wrong
                Customer customer = new Customer();
                for (Customer c : customers) {
                    if (c.getName().equals(name) && !c.getPassword().equals(password)) {
                        // Customer EXISTS, password is wrong
                        System.out.println("User already exits and password is incorrect. Try again.");
                        retry = true;
                        break;
                    }
                    else if (c.getName().equals(name) && c.getPassword().equals(password)) {
                        // Customer exists, password is right(successful LOGIN)
                        flag1 = true;
                        customer = c;
                    }
                }
                if (retry) {
                    // Customer EXISTS, password is wrong. Retry
                    continue;
                }
                if (flag1) {
                    System.out.println("You have successfully logged in.");
                }
                else {
                    System.out.println("No such user exists with the given name. Do you want to signup?");
                    System.out.println("1. Yes, 2. No");
                    int choice = sc.nextInt();
                    sc.nextLine();
                    if (choice == 1) {
                        customer = new Customer(name, password);
                        customers.add(customer);

                        // Register data for new customer in file. Opening file in Append mode
                        try (BufferedWriter file_writer = new BufferedWriter(new FileWriter("C:\\Users\\RIshi\\IdeaProjects\\ByteMeGUI\\files\\customersData.txt", true))) {
                            file_writer.newLine();
                            file_writer.write("- " + name);
                            file_writer.newLine();
                            file_writer.write(password);
                            file_writer.newLine();
                            file_writer.write(String.valueOf(false));
                            file_writer.newLine();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("You have successfully signed up.");
                    }
                    else {
                        // User not signup or logged in.
                        continue;
                    }
                }

                // CUSTOMER FUNCTIONALITIES
                while (true) {
                    customer.print_functionalities();
                    System.out.print("Which operation would you like to do?");
                    if (!sc.hasNextInt()) {                                   // check for int
                        sc.nextLine();
                        System.out.println("Please enter a number!");
                        continue;
                    }
                    int op = sc.nextInt();
                    sc.nextLine();

                    // Operations
                    if (op == 0) {                                  // No operation, quit
                        System.out.println();
                        break;
                    }
                    else if (op == 1) {
                        customer.become_vip();
                        System.out.println();
                    }
                    else if (op == 2) {
                        customer.print_menu();
                        System.out.println();
                    }
                    else if (op == 3) {
                        customer.search();
                        System.out.println();
                    }
                    else if (op == 4) {
                        customer.filter();
                        System.out.println();
                    }
                    else if (op == 5) {
                        customer.sort_menu();
                        System.out.println();
                    }
                    else if (op == 6) {
                        if (customer.isCurrent_pending_order()) {
                            System.out.println("You cannot place an order right now because you already have a pending order.");
                            continue;
                        }
                        customer.place_order();
                        System.out.println();
                    }
                    else if (op == 7) {
                        customer.view_order_status();
                        System.out.println();
                    }
                    else if (op == 8) {
                        customer.cancel_order();
                        System.out.println();
                    }
                    else if (op == 9) {
                        customer.view_order_history();
                        System.out.println();
                    }
                    else if (op == 10) {
                        customer.reorder();
                        System.out.println();
                    }
                    else {
                        System.out.println("Enter a valid choice.");
                    }
                }
            }
            else {
                // Neither admin nor customer
                System.out.println("Enter a valid choice.");
            }
        }
    }
}

