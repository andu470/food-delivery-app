package business;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Andrei Rotaru
 * This class represents the class containing information about an order
 */

public class Order implements Serializable {
    private int orderID;
    private int clientID;
    private LocalDateTime orderDate;

    public Order(int orderID, int clientID, LocalDateTime orderDate){
        this.orderID = orderID;
        this.clientID = clientID;
        this.orderDate = orderDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getClientID() {
        return clientID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    @Override
    public int hashCode(){
        int hashCode = 3;
        hashCode = hashCode + 5*orderID + 7*clientID + 11*orderDate.getDayOfMonth() + 13*orderDate.getHour();
        return hashCode;
    }

    public boolean equals(Order order){
        if(this.orderID == order.orderID){
            return true;
        }
        return false;
    }

}
