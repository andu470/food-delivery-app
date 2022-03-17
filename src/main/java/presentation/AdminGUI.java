package presentation;

import business.*;
import business.MenuItem;
import data.Serializator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Rotaru
 * This class represents the GUI for Administrator user
 */

public class AdminGUI extends JFrame implements IDeliveryServiceProcessing{
    private DeliveryService deliveryService;
    private MenuItem editingItem;

    private JTable itemsTable;
    private JTable subItemsTable;
    private final JTextField itemNameTextField = new JTextField(70);
    private final JTextField itemPriceTextField = new JTextField(30);
    private final JTextField itemRatingTextField = new JTextField(30);
    private final JTextField itemCaloriesTextField = new JTextField(30);
    private final JTextField itemProteinTextField = new JTextField(30);
    private final JTextField itemFatTextField = new JTextField(30);
    private final JTextField itemSodiumTextField = new JTextField(30);

    private final JButton backButton = new JButton("Back");
    private final JButton createItemButton = new JButton("Create Item");
    private final JButton addSubItemButton = new JButton("Add Composing Item");
    private final JButton addItemButton = new JButton("Save Item");
    private final JButton deleteItemButton = new JButton("Delete Item");
    private final JButton deleteSubItemButton = new JButton("Delete Composing Item");
    private final JButton editItemButton = new JButton("Edit Item");
    private final JButton serializeItemsButton = new JButton("Export Items");
    private final JButton deserializeItemsButton = new JButton("Import Items");

    public AdminGUI(HomeGUI homeGUI, DeliveryService deliveryService){
        this.deliveryService = deliveryService;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(200, 50, 900, 680);
        this.getContentPane().setLayout(null);
        this.setTitle("Admin");

        initializeTables();

        itemsTable.getSelectionModel().addListSelectionListener(event -> displayComposingItems(itemsTable.getSelectedRow()));

        JPanel upperPanel = new JPanel();
        upperPanel.setBounds(50,20,800,350);
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));

        itemsTable.setBounds(100,50,400,200);
        itemsTable.setRowHeight(30);
        JScrollPane itemsTablePanel = new JScrollPane(itemsTable);
        itemsTable.setFillsViewportHeight(true);
        itemsTablePanel.setBounds(100,50,400,200);
        upperPanel.add(itemsTablePanel);

        subItemsTable.setBounds(500,50,400,200);
        subItemsTable.setRowHeight(30);
        JScrollPane subItemsTablePanel = new JScrollPane(subItemsTable);
        subItemsTable.setFillsViewportHeight(true);
        subItemsTablePanel.setBounds(500,50,400,200);
        upperPanel.add(subItemsTablePanel);

        getContentPane().add(upperPanel);

        itemNameTextField.setBounds(300,410,200,30);
        getContentPane().add(itemNameTextField);
        JLabel nameLabel = new JLabel("Title:");
        nameLabel.setBounds(250,410,50,30);
        getContentPane().add(nameLabel);

        itemRatingTextField.setBounds(300,440,50,30);
        getContentPane().add(itemRatingTextField);
        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setBounds(250,440,50,30);
        getContentPane().add(ratingLabel);

        itemCaloriesTextField.setBounds(300,470,50,30);
        getContentPane().add(itemCaloriesTextField);
        JLabel caloriesLabel = new JLabel("Calories:");
        caloriesLabel.setBounds(250,470,50,30);
        getContentPane().add(caloriesLabel);

        itemProteinTextField.setBounds(300,500,50,30);
        getContentPane().add(itemProteinTextField);
        JLabel proteinLabel = new JLabel("Protein:");
        proteinLabel.setBounds(250,500,50,30);
        getContentPane().add(proteinLabel);

        itemFatTextField.setBounds(300,530,50,30);
        getContentPane().add(itemFatTextField);
        JLabel fatLabel = new JLabel("Fat:");
        fatLabel.setBounds(250,530,50,30);
        getContentPane().add(fatLabel);

        itemSodiumTextField.setBounds(300,560,50,30);
        getContentPane().add(itemSodiumTextField);
        JLabel sodiumLabel = new JLabel("Sodium:");
        sodiumLabel.setBounds(250,560,50,30);
        getContentPane().add(sodiumLabel);

        itemPriceTextField.setBounds(300,590,50,30);
        getContentPane().add(itemPriceTextField);
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(250,590,50,30);
        getContentPane().add(priceLabel);

        createItemButton.setBounds(520,410,150,30);
        createItemButton.addActionListener(e-> switchItemAddingState());
        getContentPane().add(createItemButton);

        addItemButton.setBounds(520,450,150,30);
        addItemButton.addActionListener(e->{
            String title = getProductTitle();
            List<MenuItem> menuItems = getMenuItems();

            for (MenuItem menuItem : deliveryService.getMenuItems()) {
                if (menuItem.getTitle().equals(title)) {
                    JOptionPane.showMessageDialog(this, "This product already exists");
                    switchItemAddingState();
                    return;
                }
            }
            if (title.equals("")) {
                JOptionPane.showMessageDialog(this, "The title is empty");
                return;
            }

            double rating = getProductRating();
            if(rating == -1 && menuItems.size() == 0){
                JOptionPane.showMessageDialog(this,"Wrong rating input");
                return;
            }

            int calories = getProductCalories();
            if(calories == -1 && menuItems.size() == 0){
                JOptionPane.showMessageDialog(this,"Wrong calories input");
                return;
            }

            int protein = getProductProtein();
            if(protein == -1 && menuItems.size() == 0){
                JOptionPane.showMessageDialog(this,"Wrong protein input");
                return;
            }

            int fat = getProductFat();
            if(fat == -1 && menuItems.size() == 0){
                JOptionPane.showMessageDialog(this,"Wrong fat input");
                return;
            }

            int sodium = getProductSodium();
            if(sodium == -1 && menuItems.size() == 0){
                JOptionPane.showMessageDialog(this,"Wrong sodium input");
                return;
            }

            int price = getProductPrice();
            if (price == -1 && menuItems.size() == 0) {
                JOptionPane.showMessageDialog(this, "Wrong price input");
                return;
            }

            createMenuItem(title, rating, calories, protein, fat, sodium, price, menuItems);
            switchItemAddingState();
        });
        getContentPane().add(addItemButton);

        deleteItemButton.setBounds(520,490,150,30);
        deleteItemButton.addActionListener(e->{
            int selectedRow = getItemSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No row was selected");
            } else {
                deleteMenuItem(deliveryService.getMenuItems().get(selectedRow));
            }
        });
        getContentPane().add(deleteItemButton);

        editItemButton.setBounds(520,530,150,30);
        editItemButton.addActionListener(e-> editMenuItem(null, null));
        getContentPane().add(editItemButton);

        serializeItemsButton.setBounds(520,570,150,30);
        serializeItemsButton.addActionListener(e->{
            Serializator serializator = new Serializator();
            serializator.serialize(deliveryService);
            JOptionPane.showMessageDialog(this, "Serialization confirmed");
        });
        getContentPane().add(serializeItemsButton);

        deserializeItemsButton.setBounds(520,610,150,30);
        deserializeItemsButton.addActionListener(e->{
            Serializator serializator = new Serializator();
            refreshAfterDeserialize(serializator.deserialize());
            JOptionPane.showMessageDialog(this, "Deserialization confirmed");
        });
        getContentPane().add(deserializeItemsButton);

        addSubItemButton.setBounds(680,410,200,30);
        addSubItemButton.addActionListener(e-> addComposingItem());
        getContentPane().add(addSubItemButton);

        deleteSubItemButton.setBounds(680,450,200,30);
        deleteItemButton.addActionListener(e->{
            int selectedRow = getSubItemSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No row was selected");
            } else {
                deleteSubItem(selectedRow);
            }
        });
        getContentPane().add(deleteSubItemButton);

        backButton.setBounds(50,450,100,30);
        backButton.addActionListener(e -> {
            this.setVisible(false);
            homeGUI.setVisible(true);
        });
        getContentPane().add(backButton);
    }

    private void initializeTables(){
        this.itemsTable = new JTable(new DefaultTableModel(new Object[]{"Title", "Rating","Calories",
                "Protein","Fat","Sodium","Price"}, 0));
        this.subItemsTable = new JTable(new DefaultTableModel(new Object[]{"Title", "Rating","Calories",
                "Protein","Fat","Sodium","Price"}, 0));
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();

        List<MenuItem> menuItems = deliveryService.getMenuItems();
        for(MenuItem menuItem : menuItems){
            model.addRow(new Object[]{menuItem.getTitle(), menuItem.getRating(), menuItem.getCalories(),
                    menuItem.getProtein(), menuItem.getFat(), menuItem.getSodium(), menuItem.computePrice()});
        }
    }

    public boolean deleteSubItem(int rowIndex){
        DefaultTableModel subItemsModel = (DefaultTableModel) subItemsTable.getModel();
        MenuItem editedItem;
        boolean returnValue;

        subItemsModel.removeRow(rowIndex);

        if(subItemsTable.getRowCount() == 0){
            editedItem = new BaseProduct(itemsTable.getValueAt(itemsTable.getSelectedRow(), 0).toString(),
                    0,0,0,0,0,0);
        }
        else{
            editedItem = new CompositeProduct(itemsTable.getValueAt(itemsTable.getSelectedRow(), 0).toString(),
                    this.getSubMenuItems());
        }

        MenuItem editableItem = deliveryService.getProductByTitle(editedItem.getTitle());
        returnValue = deliveryService.editMenuItem(editableItem, editedItem);

        this.displayComposingItems(this.getItemSelectedRow());
        return returnValue;
    }

    public void addComposingItem(){
        DefaultTableModel itemsModel = (DefaultTableModel) itemsTable.getModel();
        DefaultTableModel subItemsModel = (DefaultTableModel) subItemsTable.getModel();

        int rowIndex = subItemsTable.getSelectedRow();
        if(rowIndex == -1){
            JOptionPane.showMessageDialog(this, "No item was picked to be added.");
        }
        else{
            MenuItem menuItem = deliveryService.getProductByTitle(subItemsModel.getValueAt(rowIndex , 0).toString());
            itemsModel.addRow(new Object[]{menuItem.getTitle(),menuItem.getRating(),menuItem.getCalories(),
                    menuItem.getProtein(),menuItem.getFat(),menuItem.getSodium(),menuItem.computePrice()});
        }
    }

    private void displayComposingItems(int rowIndex){
        if(rowIndex == -1){
            return;
        }

        DefaultTableModel subItemsModel = (DefaultTableModel) subItemsTable.getModel();

        for(int i = subItemsTable.getRowCount()-1; i>=0; i--){
            subItemsModel.removeRow(i);
        }

        MenuItem menuItem = deliveryService.getProductByTitle(itemsTable.getValueAt(rowIndex, 0).toString());

        if(menuItem != null){
            List<MenuItem> composingItems = menuItem.getMenuItems();
            if(composingItems != null){
                for(MenuItem composingItem : composingItems){
                    subItemsModel.addRow(new Object[]{composingItem.getTitle(),composingItem.getRating(),
                            composingItem.getCalories(),composingItem.getProtein(),composingItem.getFat(),
                            composingItem.getSodium(),composingItem.computePrice()});
                }
            }
        }
        else{
            for(int i = subItemsTable.getRowCount()-1; i>=0; i--){
                subItemsModel.removeRow(i);
            }
        }
    }

    private void displayAddableItems(){
        DefaultTableModel itemsModel = (DefaultTableModel) itemsTable.getModel();
        DefaultTableModel subItemsModel = (DefaultTableModel) subItemsTable.getModel();

        for(int i = itemsTable.getRowCount()-1; i>=0; i--){
            itemsModel.removeRow(i);
        }

        for(int i = subItemsTable.getRowCount()-1; i>=0; i--){
            subItemsModel.removeRow(i);
        }

        List<MenuItem> menuItems = deliveryService.getMenuItems();
        for(MenuItem menuItem:menuItems){
            subItemsModel.addRow(new Object[]{menuItem.getTitle(), menuItem.getRating(),
                    menuItem.getCalories(), menuItem.getProtein(), menuItem.getFat(),
                    menuItem.getSodium(), menuItem.computePrice()});
        }
    }

    public void switchItemAddingState(){
        if(this.addSubItemButton.isEnabled()){
            this.itemNameTextField.setText("");
            this.itemRatingTextField.setText("");
            this.itemProteinTextField.setText("");
            this.itemCaloriesTextField.setText("");
            this.itemFatTextField.setText("");
            this.itemSodiumTextField.setText("");
            this.itemPriceTextField.setText("");
            this.createItemButton.setEnabled(true);
            this.editItemButton.setEnabled(true);
            this.addSubItemButton.setEnabled(false);
            this.addItemButton.setEnabled(false);
            this.displayExistingItems();
        }
        else{
            this.itemNameTextField.setEnabled(true);
            this.itemPriceTextField.setEnabled(true);
            this.createItemButton.setEnabled(false);
            this.editItemButton.setEnabled(false);
            this.addSubItemButton.setEnabled(true);
            this.addItemButton.setEnabled(true);
            this.displayAddableItems();
        }
    }

    public String getProductTitle(){
        return this.itemNameTextField.getText();
    }

    public double getProductRating(){
        double rating = -1;
        try{
            rating = Double.parseDouble(this.itemRatingTextField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("NumberFormatException");
        }
        return rating;
    }

    public int getProductCalories(){
        int calories = -1;
        try{
            calories = Integer.parseInt(this.itemCaloriesTextField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("NumberFormatException");
        }
        return calories;
    }

    public int getProductProtein(){
        int protein = -1;
        try{
            protein = Integer.parseInt(this.itemProteinTextField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("NumberFormatException");
        }
        return protein;
    }

    public int getProductFat(){
        int fat = -1;
        try{
            fat = Integer.parseInt(this.itemFatTextField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("NumberFormatException");
        }
        return fat;
    }

    public int getProductSodium(){
        int sodium = -1;
        try{
            sodium = Integer.parseInt(this.itemSodiumTextField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("NumberFormatException");
        }
        return sodium;
    }

    public int getProductPrice(){
        int price = -1;
        try{
            price = Integer.parseInt(this.itemPriceTextField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("NumberFormatException");
        }
        return price;
    }

    public List<MenuItem> getMenuItems(){
        List<MenuItem> items = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();

        for(int i = 0; i<itemsTable.getRowCount(); i++){
            MenuItem menuItem = deliveryService.getProductByTitle(model.getValueAt(i, 0).toString());
            items.add(menuItem);
        }
        return items;
    }

    @Override
    public MenuItem createMenuItem(String title, double rating, int calories, int protein, int fat, int sodium,
                                   int price, List<MenuItem> items) {
        return deliveryService.createMenuItem(title, rating, calories, protein, fat, sodium, price, items);
    }

    @Override
    public boolean deleteMenuItem(MenuItem removableItem) {
        boolean returnValue = deliveryService.deleteMenuItem(removableItem);
        if(returnValue){
            DefaultTableModel itemsModel = (DefaultTableModel) itemsTable.getModel();
            DefaultTableModel subItemsModel = (DefaultTableModel) subItemsTable.getModel();

            for(int i = subItemsTable.getRowCount() - 1; i>=0; i--){
                subItemsModel.removeRow(i);
            }

            for(int i = itemsTable.getRowCount() - 1; i>=0; i--){
                itemsModel.removeRow(i);
            }
            displayExistingItems();
        }
        return returnValue;
    }

    @Override
    public boolean editMenuItem(MenuItem editableItem, MenuItem editedItem) {
        if(this.editItemButton.getText().equals("Edit Item")){
            return this.beginEdit();
        }
        else{
            return this.saveChanges(editedItem);
        }
    }

    private boolean beginEdit(){
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        int rowIndex = this.itemsTable.getSelectedRow();

        if(rowIndex == -1){
            return false;
        }

        this.editingItem = this.deliveryService.getProductByTitle(model.getValueAt(rowIndex, 0).toString());

        this.itemNameTextField.setText(editingItem.getTitle());
        this.itemRatingTextField.setText(Double.toString(editingItem.getRating()));
        this.itemCaloriesTextField.setText(Integer.toString(editingItem.getCalories()));
        this.itemProteinTextField.setText(Integer.toString(editingItem.getProtein()));
        this.itemFatTextField.setText(Integer.toString(editingItem.getFat()));
        this.itemSodiumTextField.setText(Integer.toString(editingItem.getSodium()));
        this.itemPriceTextField.setText(Integer.toString(editingItem.computePrice()));
        this.itemNameTextField.setEnabled(true);
        if(editingItem.getMenuItems() == null){
            this.itemRatingTextField.setEnabled(true);
            this.itemCaloriesTextField.setEnabled(true);
            this.itemProteinTextField.setEnabled(true);
            this.itemFatTextField.setEnabled(true);
            this.itemSodiumTextField.setEnabled(true);
            this.itemPriceTextField.setEnabled(true);
        }
        this.itemsTable.setEnabled(false);
        this.addItemButton.setEnabled(false);
        this.deleteItemButton.setEnabled(false);
        this.deleteSubItemButton.setEnabled(true);
        this.addSubItemButton.setEnabled(false);
        this.editItemButton.setText("Save changes");
        return true;
    }

    private boolean saveChanges(MenuItem editedItem){
        if(subItemsTable.getRowCount() > 0){
            editedItem = new CompositeProduct(this.itemNameTextField.getText(), this.getSubMenuItems());
        }
        else{
            editedItem = new BaseProduct(this.itemNameTextField.getText(),
                    Double.parseDouble(this.itemRatingTextField.getText()),
                    Integer.parseInt(this.itemCaloriesTextField.getText()),
                    Integer.parseInt(this.itemProteinTextField.getText()),
                    Integer.parseInt(this.itemFatTextField.getText()),
                    Integer.parseInt(this.itemSodiumTextField.getText()),
                    Integer.parseInt(this.itemPriceTextField.getText()));
        }
        deliveryService.editMenuItem(this.editingItem, editedItem);

        this.itemNameTextField.setText("");
        this.itemRatingTextField.setText("");
        this.itemCaloriesTextField.setText("");
        this.itemProteinTextField.setText("");
        this.itemFatTextField.setText("");
        this.itemSodiumTextField.setText("");
        this.itemPriceTextField.setText("");
        this.itemsTable.setEnabled(true);
        this.deleteItemButton.setEnabled(true);
        this.addItemButton.setEnabled(false);
        this.addSubItemButton.setEnabled(false);
        this.deleteSubItemButton.setEnabled(false);
        this.editItemButton.setText("Edit Item");
        this.displayExistingItems();
        return true;
    }

    public List<MenuItem> getSubMenuItems(){
        List<MenuItem> items = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) subItemsTable.getModel();

        for(int i = 0; i<subItemsTable.getRowCount(); i++){
            MenuItem menuItem = deliveryService.getProductByTitle(model.getValueAt(i, 0).toString());
            items.add(menuItem);
        }
        return items;
    }

    @Override
    public Order createOrder(int clientID, List<MenuItem> menuItems) {
        return null;
    }

    @Override
    public int computeOrderPrice(Order order) {
        return 0;
    }

    @Override
    public boolean generateBill(Order order) {
        return false;
    }

    private void displayExistingItems(){
        DefaultTableModel itemsModel = (DefaultTableModel) itemsTable.getModel();
        DefaultTableModel subItemsModel = (DefaultTableModel) subItemsTable.getModel();

        for(int i = itemsTable.getRowCount()-1; i>=0; i--){
            itemsModel.removeRow(i);
        }

        for(int i = subItemsTable.getRowCount()-1; i>=0; i--){
            subItemsModel.removeRow(i);
        }

        List<MenuItem> menuItems = deliveryService.getMenuItems();
        for(MenuItem menuItem : menuItems){
            itemsModel.addRow(new Object[]{menuItem.getTitle(),menuItem.getRating(),
                    menuItem.getCalories(),menuItem.getProtein(), menuItem.getFat(),
                    menuItem.getSodium(), menuItem.computePrice()});
        }
    }

    public void refreshAfterDeserialize(DeliveryService deliveryService){
        this.deliveryService = deliveryService;
        this.displayExistingItems();
    }

    public int getItemSelectedRow(){
        return itemsTable.getSelectedRow();
    }

    public int getSubItemSelectedRow(){
        return subItemsTable.getSelectedRow();
    }
}
