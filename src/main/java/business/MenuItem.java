package business;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andrei Rotaru
 * This class represents an abstract class, having methods implemented in BaseProduct and CompositeProduct
 */

public abstract class MenuItem implements Serializable {

    public abstract String getTitle();
    public abstract double getRating();
    public abstract int getCalories();
    public abstract int getProtein();
    public abstract int getFat();
    public abstract int getSodium();
    public abstract int computePrice();
    public abstract boolean addItem(MenuItem item);
    public abstract boolean removeItem(MenuItem item);
    public abstract boolean existItem(MenuItem item);
    public abstract boolean editItem(MenuItem editedItem);
    public abstract List<MenuItem> getMenuItems();
}
