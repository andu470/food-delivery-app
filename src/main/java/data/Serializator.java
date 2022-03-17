package data;

import business.DeliveryService;

import java.io.*;

/**
 * @author Andrei Rotaru
 * This class implements method for serialize and deserialize data from/in a .ser file
 */

public class Serializator {

    public boolean serialize(DeliveryService deliveryService){
        try {
            FileOutputStream file = new FileOutputStream("del.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(deliveryService);
            out.close();
            file.close();
        } catch (IOException e) {
            System.out.println("Error serializing");
            return false;
        }
        return true;
    }

    public DeliveryService deserialize(){
        try {
            DeliveryService deliveryService = new DeliveryService();
            FileInputStream file = new FileInputStream("del.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            deliveryService = (DeliveryService) in.readObject();
            in.close();
            file.close();
            return deliveryService;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error deserializing");
            return null;
        }
    }
}
