package Users;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class CanteenGUI {
    private JFrame frame;
    private JPanel main_panel;
    public CardLayout card_layout;

    private JTable menu_table;
    private JTable orders_table;

    public CanteenGUI(ArrayList<FoodItem> menu, PriorityQueue<Order> pending_orders) {
        frame = new JFrame("Byte Me!");
        card_layout = new CardLayout();
        main_panel = new JPanel(card_layout);

        JPanel menu_page = create_menu_page(menu);
        main_panel.add(menu_page, "Canteen Menu");

        JPanel orders_page = create_orders_page(pending_orders);
        main_panel.add(orders_page, "Pending Orders");

        frame.add(main_panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel create_menu_page(ArrayList<FoodItem> menu) {
        JPanel menu_panel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("Canteen Menu", JLabel.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 24));

        // creating the menu table using tableModel
        String[] columns = {"Name", "Category", "Price", "Available"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for (FoodItem item : menu) {
            tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.isAvailable() ? "Yes" : "No"       // if availability: true, then yes, false: no
            });
        }
        this.menu_table = new JTable(tableModel);
        JScrollPane scroll_pane = new JScrollPane(menu_table);

        // button for switching to pending orders
        JButton switch_to_orders = new JButton("Go to Orders");
        switch_to_orders.addActionListener(element -> card_layout.show(main_panel, "Pending Orders"));

        menu_panel.add(header, BorderLayout.NORTH);
        menu_panel.add(scroll_pane, BorderLayout.CENTER);
        menu_panel.add(switch_to_orders, BorderLayout.SOUTH);

        return menu_panel;
    }

    private JPanel create_orders_page(PriorityQueue<Order> orders) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("Pending Orders", JLabel.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 24));

        String[] columns = {"Order No.", "Customer", "No. of Distinct Items Ordered", "Total Price", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        int order_index = 1;
        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                    order_index,
                    order.getCustomer().getName(),
                    order.getItems().size(),
                    order.getTotal_price(),
                    order.getStatus()
            });
            order_index++;
        }
        this.orders_table = new JTable(tableModel);
        JScrollPane scroll_pane = new JScrollPane(orders_table);

        // button for switching to menu
        JButton switch_to_menu = new JButton("Go to Menu");
        switch_to_menu.addActionListener(element -> card_layout.show(main_panel, "Canteen Menu"));

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll_pane, BorderLayout.CENTER);
        panel.add(switch_to_menu, BorderLayout.SOUTH);

        return panel;
    }

    public void refresh_menu() {
        DefaultTableModel tableModel = (DefaultTableModel) this.menu_table.getModel();
        tableModel.setRowCount(0);                              // clear existing rows

        // after some modification has been done in the menu, this method rebuilds the menu again
        for (FoodItem item : FoodItem.menu) {
            tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.isAvailable() ? "Yes" : "No"
            });
        }
    }

    public void refresh_orders() {
        DefaultTableModel tableModel = (DefaultTableModel) this.orders_table.getModel();
        tableModel.setRowCount(0);

        int order_index = 1;
        for (Order order : Order.orders) {
            tableModel.addRow(new Object[]{
                    order_index,
                    order.getCustomer().getName(),
                    order.getItems().size(),
                    order.getTotal_price(),
                    order.getStatus()
            });
        }
    }

    public JPanel getMain_panel() {
        return main_panel;
    }
}
