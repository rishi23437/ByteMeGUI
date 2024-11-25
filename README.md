# Byte Me! - Food Ordering System
Byte Me! is a GUI and CLI combined Food Ordering System, which manages and facilitates Customer and Staff operations. It uses AWT and Swing to manage the Graphical User Interface, file handling for some features like menu, customer data, pending orders and order history(for each customer). I made this application in Java as a part of my Advanced Programming Course.

## Assumptions - Assignment 4
- Enter **only 1 word** for the name and category of a Food Item!

### GUI Enhancements


### I/O Stream Management
- I am managing the data of customers only, not admins.
- 

### JUnit Testing


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
