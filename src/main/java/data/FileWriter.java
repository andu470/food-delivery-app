package data;

import business.MenuItem;
import business.Order;

import java.io.*;
import java.util.List;

/**
 * @author Andrei Rotaru
 * This class implements methods for writing in a file (generating bills)
 */

public class FileWriter {

    public boolean writeBill(Order order, List<MenuItem> menuItems, int totalPrice){
        String text = createOutputText(order, menuItems, totalPrice);
        try{
            File file = new File("bills.txt");
            BufferedWriter wrt = new BufferedWriter(new java.io.FileWriter(file,true));
            wrt.write(text);
            wrt.close();
            return true;
        }catch(FileNotFoundException e){
            System.out.println("Output file is not valid!");
            return false;
        } catch (IOException e) {
            System.out.println("Incorrect output format!");
            return false;
        }
    }

    private String createOutputText(Order order, List<MenuItem> menuItems, int totalPrice){
        String text = "";
        text += "Date: " + order.getOrderDate().toString().replace("T", " ").substring(0, 19) + "\n";
        text += "Client: " + order.getClientID() + "\n";
        for(MenuItem menuItem:menuItems){
            String title = menuItem.getTitle();
            int price = menuItem.computePrice();
            text += title + " ";
            text += price + "\n";
        }
        text += "Total: ";
        text += totalPrice + "\n\n";
        return text;
    }
}
