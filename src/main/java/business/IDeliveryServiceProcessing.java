package business;

import java.util.List;

/**
 * @author Andrei Rotaru
 * This class represents an interface, haveing methods implemented in DeliveryService, in order to comunicate
 * with others layers
 */

public interface IDeliveryServiceProcessing {
    MenuItem createMenuItem(String title, double rating, int calories, int protein, int fat,
                            int sodium, int price, List<MenuItem> items);

    boolean deleteMenuItem(MenuItem removableItem);

    boolean editMenuItem(MenuItem editableItem, MenuItem editedItem);

    Order createOrder(int clientID, List<MenuItem> menuItems);

    int computeOrderPrice(Order order);

    boolean generateBill(Order order);
}
