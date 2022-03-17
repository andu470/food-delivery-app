package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Rotaru
 * This class represents the composite class in the Composite Design Pattern
 */

public class CompositeProduct extends MenuItem implements Serializable {
    private String title;
    private double rating;
    private int calories;
    private int protein;
    private int fat;
    private int sodium;
    private int price;
    private List<MenuItem> menuItems;

    public CompositeProduct(String title, List<MenuItem> menuItems){
        this.title = title;
        this.menuItems = new ArrayList<>(menuItems);
        this.price = this.computePrice();
        this.rating = getRating();
        this.calories = getCalories();
        this.protein = getProtein();
        this.fat = getFat();
        this.sodium = getSodium();
    }

    public int computePrice(){
        this.price = 0;
        if(this.menuItems != null){
            for(MenuItem item : this.menuItems){
                price = price + item.computePrice();
            }
        }
        return this.price;
    }

    @Override
    public double getRating() {
        return 0;
    }

    @Override
    public int getCalories() {
        return 0;
    }

    @Override
    public int getProtein() {
        return 0;
    }

    @Override
    public int getFat() {
        return 0;
    }

    @Override
    public int getSodium() {
        return 0;
    }

    public boolean editItem(MenuItem editedItem){
        this.title = editedItem.getTitle();
        this.rating = editedItem.getRating();
        this.calories = editedItem.getCalories();
        this.protein = editedItem.getProtein();
        this.fat = editedItem.getFat();
        this.sodium = editedItem.getSodium();
        this.price = editedItem.computePrice();
        this.menuItems = editedItem.getMenuItems();
        return true;
    }

    public boolean addItem(MenuItem item){
        menuItems.add(item);
        this.rating = item.getRating();
        this.calories = item.getCalories();
        this.protein = item.getProtein();
        this.fat = item.getFat();
        this.sodium = item.getSodium();
        this.price = this.computePrice();
        return true;
    }

    public boolean removeItem(MenuItem item){
        boolean success = menuItems.remove(item);
        this.rating = item.getRating();
        this.calories = item.getCalories();
        this.protein = item.getProtein();
        this.fat = item.getFat();
        this.sodium = item.getSodium();
        this.price = this.computePrice();
        return success;
    }

    public boolean existItem(MenuItem searchedItem){
        for(MenuItem menuItem: this.menuItems){
            if(menuItem == searchedItem){
                return true;
            }
            if(menuItem.existItem(searchedItem)){
                return true;
            }
        }
        return false;
    }

    public String getTitle(){
        return this.title;
    }

    public List<MenuItem> getMenuItems(){
        return this.menuItems;
    }
}
