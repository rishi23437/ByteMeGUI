package Users;

import java.util.Comparator;

public class MenuBoughtComparator implements Comparator<FoodItem> {
    /*
    1. Sorts in DESCENDING of bought.
    2. So, most popular item(bought the most) is the first item.
    */
    @Override
    public int compare(FoodItem f1, FoodItem f2) {
        if (f1.bought > f2.bought) {
            return -1;
        }
        else if (f1.bought < f2.bought) {
            return 1;
        }
        return 0;
    }
}
