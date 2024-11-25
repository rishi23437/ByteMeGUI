package Users;

import java.util.Comparator;

public class MenuPriceComparator implements Comparator<FoodItem> {
    @Override
    public int compare(FoodItem i1, FoodItem i2) {
        if (i1.getPrice() > i2.getPrice()) {
            return 1;
        }
        else if (i1.getPrice() < i2.getPrice()) {
            return -1;
        }
        return 0;
    }
}
