# Byte Me! - Food Ordering System
Byte Me! is a GUI and CLI combined Food Ordering System, which manages and facilitates Customer and Staff operations. It uses AWT and Swing to manage the Graphical User Interface, file handling for some features like menu, customer data, pending orders and order history(for each customer). I made this application in Java as a part of my Advanced Programming Course.

## Assumptions - Assignment 4
- Enter **only 1 word** for the name and category of a Food Item!
- Try to enter 1 word only for customer name too

### GUI Enhancements
- Coherent with my assumption in Assignment 3, an order can have only 4 statuses - Pending, Completed, Cancelled and Denied. Therefore, all the orders on the Pending Orders page have their status as Pending.
- For Orders, instead of displaying all the items with their details(name, category, price, special request), I am displaying number of items bought. To get the details of the items, view_pending_orders() method of CLI can be run.

### I/O Stream Management
- I am managing the data of customers only, not admins.

### JUnit Testing
- Because my code is different(handles the whole process of placing an order in one function), implementing the **Out-of-stock test** on that function was not possible. Therefore, I have **copied** the part where we add an item into a different method, and tested it on that method. It can be easily checked that the 2 parts are the same.
- Similarly, I had NOT made any function for login and signup, I just implemented it in the main() method. So, for the JUnit part, I transfered the login code into a method called login in the InvalidLoginAttemptsTest class.

## Functionality Aspects
### I/O Stream Management
- FORMAT for CUSTOMER DATA FILE. Some of these start with symbols(-, +, =) to depict what the line contains:
  - "- name"
  - "password"
  - "vip(true/false)"
  - order history:
    - "+ total_price status"
    - "= name category price quantity special_request" (FoodItem lines)

- FORMAT for MENU FILE:
  - "- name"
  - category
  - price
  - availability

- FORMAT for PENDING ORDERS FILE
  - "+ CustomerName total_price status"
  - "= name category price quantity special request" (FoodItem lines)