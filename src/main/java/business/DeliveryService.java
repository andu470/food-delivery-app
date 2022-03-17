package business;

import data.CsvReader;
import data.FileWriter;
import presentation.EmployeeGUI;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andrei Rotaru
 * This class represents the "brain" of the entire application, containing the items from menu and the orders
 */

public class DeliveryService extends Observable implements IDeliveryServiceProcessing, Serializable {

    private Map<Order, List<MenuItem>> orders;
    private List<MenuItem> menuItems;

    public DeliveryService(){
        this.orders = new Hashtable<Order, List<MenuItem>>();
        this.menuItems = getMenuItemsFromFile();
//        this.menuItems = new ArrayList<>();
    }

    @Override
    public MenuItem createMenuItem(String title, double rating, int calories, int protein,
            int fat, int sodium,int price, List<MenuItem> items){
        MenuItem item;
        if(items.size() == 0){
            item = new BaseProduct(title, rating, calories, protein, fat, sodium, price);
        }
        else{
            item = new CompositeProduct(title, items);
        }
        this.menuItems.add(item);
        return item;
    }

    @Override
    public boolean editMenuItem(MenuItem editableItem, MenuItem editedItem){
        if(editableItem.getMenuItems() != null && editedItem.getMenuItems() == null){
            return this.menuItems.remove(editableItem);
        }
        else{
            return editableItem.editItem(editedItem);
        }
    }

    @Override
    public boolean deleteMenuItem(MenuItem menuItem){
        List<MenuItem> removableItems = new ArrayList<>();
        for(MenuItem menuItem1 : this.menuItems){
            if(menuItem1.existItem(menuItem)){
                removableItems.add(menuItem1);
            }
        }
        removableItems.add(menuItem);
        this.menuItems.removeAll(removableItems);
        return true;
    }

    @Override
    public Order createOrder(int clientID, List<MenuItem> menuItems){
        LocalDateTime date = LocalDateTime.now();
        Order order = new Order(this.computeNextOrderId(), clientID, date);
        boolean notification = false;

        orders.put(order, menuItems);
        for(MenuItem menuItem : menuItems){
            if(menuItem.getMenuItems() != null){
                notification = true;
            }
        }

        if(notification){
            setChanged();
            notifyObservers(order);
        }

        return order;
    }

    private int computeNextOrderId(){
        Set<Order> orders = this.orders.keySet();
        int nextID = orders.size() + 1;
        return nextID;
    }

    @Override
    public int computeOrderPrice(Order order){
        int price = 0;
        for(MenuItem menuItem:this.orders.get(order)){
            price = price + menuItem.computePrice();
        }

        return price;
    }

    @Override
    public boolean generateBill(Order order){
        boolean success;

        FileWriter writer = new FileWriter();
        success = writer.writeBill(order, this.orders.get(order), this.computeOrderPrice(order));

        return success;
    }

    public MenuItem getProductByTitle(String title){
        for(MenuItem item:this.menuItems){
            if(item.getTitle().equals(title)){
                return item;
            }
        }
        return null;
    }

    public void addEmployeeGUIObserver(EmployeeGUI employeeGUI){
        this.addObserver(employeeGUI);
    }

    public Set<Order> getOrders(){
        return this.orders.keySet();
    }

    public List<MenuItem> getOrderItems(Order order){
        return Collections.unmodifiableList(this.orders.get(order));
    }

    public List<MenuItem> getMenuItems(){
        return Collections.unmodifiableList(this.menuItems);
    }

    public List<MenuItem> getMenuItemsByOrder(Order order){
        return orders.get(order);
    }

    public ArrayList<MenuItem> getMenuItemsFromFile(){
        CsvReader csvReader = new CsvReader();
        return csvReader.readFromCsv();
    }

    public List<MenuItem> searchByTitle(String title){
        List<MenuItem> menuItemsFiltred;
        menuItemsFiltred = this.menuItems.stream().filter(p -> p.getTitle().contains(title)).collect(Collectors.toList());
        return menuItemsFiltred;
    }

}
