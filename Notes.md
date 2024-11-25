# Notes
- GUI will only display the menu and pending orders(other pages are optional) and will not allow updates to their status
- Updates and other operations should be done through CLI only
- Updates performed by the CLI should be reflected in the GUI. GUI only serves as a display interface
- Updates, etc in this assignment are handled using files. So, they are saved after each 
- Conclusion: customer list toh chahiye hoga
- I/O stream management - Manage user data
  - FORMAT for CUSTOMER DATA FILE -
  - "- name"
  - "password"
  - "vip(true/false)"
  - order history:
    - "+ total_price status"
    - "= name category price quantity special_request" (FoodItem lines)
  - In customer data file, Only write important parts of the order which you wish to retrieve
  - Login: Retrieve data from customers list - DONE
  - SIGNUP - SAVE DATA TO FILE. LOGOUT: MODIFY SAVED DATA(ORDER HISTORY, VIP).
  - Logout wala probably not required, if you update vip and order history when they are actually updated
  - Each time an ORDER of a customer is COMPLETED, CANCELLED OR DENIED(basically, not pending), WRITE ORDER DATA TO FILE
  - REMEMBER: While writing to a file, WHICHEVER LINE YOU WRITE AT LAST, GO TO THE NEXT LINE!!!
  - Enter **only 1 word** for the name and category of a Food Item!
- GUI
  - Allow GUI viewing for both admin and customer. Technically, View menu should be for Customer, and view pending orders for Admin, based on Assignment 3
  - Possible plan: view_menu: for Customer, execute the method which displays GUI menu. for Admin, provide an option for the same


# Current Task
- GUI
  - REMEMBER: for add, update and delete item, you HAVE to MODIFY the respective things in the MENU FILE as well
  - ORDER IS PLACED: add the order details to the Orders file
  - Order is completed, cancelled or denied - remove from Orders file
  - Button for navigation


- I/O stream management - Manage user data ---------------- DONE
  - vip: update in file ----------------------------------- DONE
  - signup: save data ------------------------------------- DONE
  - new order (not pending): update order history in file - DONE

  
