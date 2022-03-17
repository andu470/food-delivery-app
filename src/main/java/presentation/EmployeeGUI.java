package presentation;

import business.DeliveryService;
import business.Order;
import business.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Andrei Rotaru
 * This class represents the GUI for the Employee user, receiving information about a new order
 */

public class EmployeeGUI extends JFrame implements Observer {
    private final JButton backButton;
    private final JPanel upperPanel;
    private JTable itemsTable;

    public EmployeeGUI(HomeGUI homeGUI) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(100, 50, 700, 700);
        this.getContentPane().setLayout(null);
        this.setTitle("Employee");

        itemsTable = new JTable();
        this.itemsTable = new JTable(new DefaultTableModel(new Object[]{"Date", "Client", "Title"},
                0));

        upperPanel = new JPanel();
        upperPanel.setBounds(150,70,400,400);
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));
        itemsTable.setBounds(100,100,200,200);
        itemsTable.setRowHeight(30);

        JScrollPane itemsTablePanel = new JScrollPane(itemsTable);
        itemsTable.setFillsViewportHeight(true);
        itemsTablePanel.setBounds(120,120,300,525);

        upperPanel.add(itemsTablePanel);
        this.getContentPane().add(upperPanel);

        backButton = new JButton("Back");
        backButton.setBounds(100, 550, 100, 30);
        backButton.addActionListener(e -> {
            this.setVisible(false);
            homeGUI.setVisible(true);
        });
        getContentPane().add(backButton);
    }

    @Override
    public void update(Observable o, Object obj) {
        DeliveryService deliveryService;
        Order order;

        if(o instanceof DeliveryService){
            deliveryService = (DeliveryService) o;
        }
        else{
            return;
        }

        if(obj instanceof Order){
            order = (Order)obj;
        }
        else{
            return;
        }
        this.update(deliveryService, order);
    }

    private void update(DeliveryService deliveryService, Order order) {
        StringBuilder sb = new StringBuilder();
        List<MenuItem> items = deliveryService.getOrderItems(order);

        updateJTable(order, items);
        for(MenuItem menuItem: items){
            if(menuItem.getMenuItems() != null){
                sb.append(menuItem.getTitle() + ",");
            }
        }
        sb.deleteCharAt(sb.length()-1);

        JOptionPane.showMessageDialog(this, "A new order was placed: " + sb);
    }

    private void updateJTable(Order order, List<MenuItem> menuItems){
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        DefaultTableModel itemsModel = (DefaultTableModel) itemsTable.getModel();

        for(MenuItem menuItem : menuItems){
            if(menuItem.getMenuItems() != null){
                itemsModel.addRow(new Object[]{date.format(order.getOrderDate()), order.getClientID(),
                        menuItem.getTitle()});
            }
        }
    }
}
