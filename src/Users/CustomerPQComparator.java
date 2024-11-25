package Users;

import java.util.Comparator;

public class CustomerPQComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2) {
        // Comparing the priority of 2 orders
        if (order1.getCustomer().isVip() && !order2.getCustomer().isVip()) {
            // Customer 1 is VIP, Customer 2 is not
            return -1;
        }
        if (!order1.getCustomer().isVip() && order2.getCustomer().isVip()) {
            // Customer 2 is VIP, Customer 1 is not
            return 1;
        }
        // Both VIPs, or neither VIPs: compare by arrival_time
        return order1.getArrival_time().compareTo(order2.getArrival_time());
    }
}
