package data;

import business.BaseProduct;
import business.MenuItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Andrei Rotaru
 * This class implements methods for reading information from a .csv file + eliminating duplicates
 */

public class CsvReader {

    public ArrayList<MenuItem> readFromCsv(){
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        try{
            BufferedReader buffer = new BufferedReader(new FileReader("products.csv"));
            menuItems = buffer.lines().skip(1).map(line -> line.split(",")).map(fields->
                    new BaseProduct(fields[0], Double.parseDouble(fields[1]), Integer.parseInt(fields[2]),
                            Integer.parseInt(fields[3]), Integer.parseInt(fields[4]),Integer.parseInt(fields[5]),
                            Integer.parseInt(fields[6]))).collect(Collectors.toCollection(ArrayList::new));

            ArrayList<MenuItem> auxMenuItems = (ArrayList<MenuItem>)(menuItems.stream().filter(distinctByTitle(e ->
                    e.getTitle())).collect(Collectors.toList()));
            menuItems = auxMenuItems;

        }catch (IOException e){
            System.out.println("Error reading from csv");
        }
        return menuItems;
    }

    //code taken from https://stackoverflow.com/questions/53164975/extract-duplicate-objects-from-a-list-in-java-8
    public static <T> Predicate<T> distinctByTitle(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
