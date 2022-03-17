package business;

import java.util.*;
import java.io.Serializable;

/**
 * @author Andrei Rotaru
 * This class represents a leaf class in the Composite Design Pattern
 */

public class BaseProduct extends MenuItem implements Serializable {
    private String title;
    private double rating;
    private int calories;
    private int protein;
    private int fat;
    private int sodium;
    private int price;

    public BaseProduct(String title, double rating, int calories, int protein, int fat, int sodium, int price){
        this.title = title;
        this.rating = rating;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
        this.price = price;
    }

    public boolean editItem(MenuItem editedItem){
        this.title = editedItem.getTitle();
        this.rating = editedItem.getRating();
        this.calories = editedItem.getCalories();
        this.protein = editedItem.getProtein();
        this.fat = editedItem.getFat();
        this.sodium = editedItem.getSodium();
        this.price = editedItem.computePrice();
        return true;
    }

    public int computePrice(){
        return this.price;
    }

    public boolean addItem(MenuItem item){
        return false;
    }

    public boolean removeItem(MenuItem item) {
        return false;
    }

    public boolean existItem(MenuItem item){
        if (this == item){
            return true;
        }
        return false;
    }

    public String getTitle(){
        return this.title;
    }

    @Override
    public double getRating() {
        return rating;
    }

    @Override
    public int getCalories() {
        return calories;
    }

    @Override
    public int getProtein() {
        return protein;
    }

    @Override
    public int getFat() {
        return fat;
    }

    @Override
    public int getSodium() {
        return sodium;
    }

    public List<MenuItem> getMenuItems(){
        return null;
    }
}
