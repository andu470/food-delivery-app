package presentation;

import business.DeliveryService;
import business.IDeliveryServiceProcessing;
import business.MenuItem;
import business.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Andrei Rotaru
 * This class represents the GUI for the Client user
 */

public class ClientGUI extends JFrame implements IDeliveryServiceProcessing {
    DeliveryService deliveryService;

    private final JTable ordersTable;
    private final JTable orderItemsTable;
    private final JButton backButton = new JButton("Back");
    private final JButton createOrderButton = new JButton("Place Order");
    private final JButton addItemButton = new JButton("Add Item");
    private final JButton computePriceButton = new JButton("Compute Price");
    private final JButton generateBillButton = new JButton("Generate Bill");
    private final JLabel priceLabel = new JLabel("Total sum:");
    private final JTextField priceTextField = new JTextField();

    private final JButton searchButton = new JButton("Search");

    private final JTextField titleTextField = new JTextField();
    private final JLabel titleLabel = new JLabel("Title");

    public ClientGUI(HomeGUI homeGUI, DeliveryService deliveryService){
        this.deliveryService = deliveryService;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(200, 50, 800, 700);
        this.getContentPane().setLayout(null);
        this.setTitle("Client");

        this.ordersTable = new JTable(new DefaultTableModel(new Object[]{"OrderID", "Date"}, 0));
        this.orderItemsTable = new JTable(new DefaultTableModel(new Object[]{"Title", "Rating", "Calories",
                "Protein", "Fat", "Sodium", "Price"}, 0));

        displayOrders();

        addTableHandlers();

        JPanel upperPanel = new JPanel();
        upperPanel.setBounds(50,50,700,400);
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));

        ordersTable.setBounds(100,50,200,200);
        ordersTable.setRowHeight(30);
        JScrollPane ordersTablePanel = new JScrollPane(ordersTable);
        ordersTable.setFillsViewportHeight(true);
        ordersTablePanel.setBounds(100,50,200,200);
        upperPanel.add(ordersTablePanel);

        orderItemsTable.setBounds(300,50,400,200);
        orderItemsTable.setRowHeight(30);
        JScrollPane ordersItemsTablePanel = new JScrollPane(orderItemsTable);
        orderItemsTable.setFillsViewportHeight(true);
        ordersItemsTablePanel.setBounds(300,50,400,200);
        upperPanel.add(ordersItemsTablePanel);

        getContentPane().add(upperPanel);

        createOrderButton.setBounds(200,450,150,30);
        createOrderButton.addActionListener(e-> createNewOrder());
        getContentPane().add(createOrderButton);

        addItemButton.setBounds(200,500,150,30);
        addItemButton.addActionListener(e-> addSelectedItemToOrder());
        getContentPane().add(addItemButton);

        computePriceButton.setBounds(200,550,150,30);
        computePriceButton.addActionListener(e->{
            Order order = getCurrentOrder();
            if (order == null) {
                JOptionPane.showMessageDialog(this, "No order was selected");
            } else {
                computeOrderPrice(order);
            }
        });
        getContentPane().add(computePriceButton);

        generateBillButton.setBounds(200,600,150,30);
        generateBillButton.addActionListener(e->{
            Order order = getCurrentOrder();
            if (order == null) {
                JOptionPane.showMessageDialog(this, "No order was selected");
            } else {
                generateBill(order);
            }
        });
        getContentPane().add(generateBillButton);

        priceLabel.setBounds(380,520,150,30);
        getContentPane().add(priceLabel);

        priceTextField.setBounds(380,550,100,30);
        getContentPane().add(priceTextField);

        titleLabel.setBounds(550,450,150,30);
        getContentPane().add(titleLabel);

        titleTextField.setBounds(550,480,200,30);
        getContentPane().add(titleTextField);

        searchButton.setBounds(550,530,150,30);
        searchButton.addActionListener(e-> searchTitle(titleTextField));
        getContentPane().add(searchButton);

        backButton.setBounds(50,600,100,30);
        backButton.addActionListener(e -> {
           this.setVisible(false);
           homeGUI.setVisible(true);
        });
        getContentPane().add(backButton);
    }

    private void addTableHandlers(){
        this.ordersTable.getSelectionModel().addListSelectionListener(event -> displayOrderItems(ordersTable.getSelectedRow()));
    }

    private void displayOrderItems(int rowIndex){
        if(rowIndex == -1){
            return;
        }
        Set<Order> orders = deliveryService.getOrders();
        DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
        DefaultTableModel orderItemsModel = (DefaultTableModel) orderItemsTable.getModel();
        Order crtOrder = null;

        for(int i = orderItemsTable.getRowCount()-1; i>=0; i--){
            orderItemsModel.removeRow(i);
        }

        for(Order order : orders){
            if(order.getOrderID() == Integer.parseInt(ordersModel.getValueAt(rowIndex, 0).toString())){
                crtOrder = order;
                break;
            }
        }

        if(crtOrder!=null){
            List<MenuItem> items = deliveryService.getMenuItemsByOrder(crtOrder);
            for(MenuItem item : items){
                orderItemsModel.addRow(new Object[]{item.getTitle(),item.getRating(), item.getCalories(),
                        item.getProtein(), item.getFat(), item.getSodium(), item.computePrice()});
            }
        }
    }

    public void createNewOrder(){
        if(this.createOrderButton.getText().equals("Place Order")){
            DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
            DefaultTableModel orderItemsModel = (DefaultTableModel) orderItemsTable.getModel();

            for(int i = orderItemsTable.getRowCount()-1; i>=0; i--){
                orderItemsModel.removeRow(i);
            }
            for(int i = ordersTable.getRowCount()-1; i>=0; i--){
                ordersModel.removeRow(i);
            }
            List<MenuItem> items = deliveryService.getMenuItems();
            for(MenuItem item : items){
                orderItemsModel.addRow(new Object[]{item.getTitle(), item.getRating(), item.getCalories(),
                        item.getProtein(), item.getFat(), item.getSodium(), item.computePrice()});
            }
            switchDesign();
        }
        else{
            DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
            DefaultTableModel orderItemsModel = (DefaultTableModel) orderItemsTable.getModel();
            List<MenuItem> items = this.getSelectedItems();
            orderItemsTable.getSelectionModel().clearSelection();

            for(int i = orderItemsTable.getRowCount()-1; i>=0; i--){
                orderItemsModel.removeRow(i);
            }
            for(int i = ordersTable.getRowCount()-1; i>=0; i--){
                ordersModel.removeRow(i);
            }

            if(items.size() > 0){
                this.createOrder(0, items);
            }
            else{
                JOptionPane.showMessageDialog(this, "No product was selected");
            }

            switchDesign();
        }
    }

    private void switchDesign(){
        if (this.createOrderButton.getText().equals("Finish Order")){
            this.createOrderButton.setText("Place Order");
            this.addItemButton.setEnabled(false);
            changeColumns();
            this.displayOrders();
        }
        else{
            this.createOrderButton.setText("Finish Order");
            this.addItemButton.setEnabled(true);
            changeColumns();
        }
    }

    private void changeColumns(){
        DefaultTableModel ordersModel = (DefaultTableModel)ordersTable.getModel();
        ordersModel.setColumnCount(0);

        if(this.createOrderButton.getText().equals("Place Order")){
            ordersModel.addColumn("OrderID");
            ordersModel.addColumn("Date");
            ordersModel.addColumn("ClientID");
        }
        else{
            ordersModel.addColumn("Name");
            ordersModel.addColumn("Price");
        }
    }

    private List<MenuItem> getSelectedItems(){
        DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
        List<MenuItem> items = new ArrayList<>();

        for(int i = 0; i<ordersModel.getRowCount(); i++){
            MenuItem item = deliveryService.getProductByTitle(ordersModel.getValueAt(i, 0).toString());
            items.add(item);
        }

        return items;
    }

    private void displayOrders(){
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
        Set<Order> orders = deliveryService.getOrders();

        for(Order order:orders){
            ordersModel.addRow(new Object[]{order.getOrderID(), date.format(order.getOrderDate()),
                    order.getClientID()});
        }
    }

    @Override
    public MenuItem createMenuItem(String title, double rating, int calories, int protein, int fat, int sodium, int price, List<MenuItem> items) {
        return null;
    }

    @Override
    public boolean deleteMenuItem(MenuItem removableItem) {
        return false;
    }

    @Override
    public boolean editMenuItem(MenuItem editableItem, MenuItem editedItem) {
        return false;
    }

    @Override
    public Order createOrder(int clientID, List<MenuItem> menuItems) {
        return deliveryService.createOrder(clientID, menuItems);
    }

    @Override
    public int computeOrderPrice(Order order) {
        int returnValue = deliveryService.computeOrderPrice(order);
        if(returnValue > 0){
            this.priceTextField.setText(Integer.toString(returnValue));
        }
        return returnValue;
    }

    @Override
    public boolean generateBill(Order order) {
        boolean returnValue = deliveryService.generateBill(order);
        if(returnValue){
            JOptionPane.showMessageDialog(this, "Bill generated");
        }
        return returnValue;
    }

    public void addSelectedItemToOrder(){
        DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
        DefaultTableModel orderItemsModel = (DefaultTableModel) orderItemsTable.getModel();
        int rowIndex = orderItemsTable.getSelectedRow();
        MenuItem item = this.deliveryService.getProductByTitle(orderItemsModel.getValueAt(rowIndex, 0).toString());
        ordersModel.addRow(new Object[]{item.getTitle(), item.computePrice()});
    }

    public Order getCurrentOrder(){
        if(this.ordersTable.getSelectedRow() < 0){
            return null;
        }
        DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
        Set<Order> orders = this.deliveryService.getOrders();
        for(Order order:orders){
            if(order.getOrderID() == Integer.parseInt(ordersModel.getValueAt(this.ordersTable.getSelectedRow(), 0).toString())){
                return order;
            }
        }
        return null;
    }

    public void searchTitle(JTextField title){
        DefaultTableModel ordersModel = (DefaultTableModel) ordersTable.getModel();
        DefaultTableModel orderItemsModel = (DefaultTableModel) orderItemsTable.getModel();

        for(int i = orderItemsTable.getRowCount()-1; i>=0; i--){
            orderItemsModel.removeRow(i);
        }
        for(int i = ordersTable.getRowCount()-1; i>=0; i--){
            ordersModel.removeRow(i);
        }
        List<MenuItem> items = deliveryService.searchByTitle(title.getText());
        for(MenuItem item : items){
            orderItemsModel.addRow(new Object[]{item.getTitle(), item.getRating(), item.getCalories(),
                    item.getProtein(), item.getFat(), item.getSodium(), item.computePrice()});
        }
    }
}
