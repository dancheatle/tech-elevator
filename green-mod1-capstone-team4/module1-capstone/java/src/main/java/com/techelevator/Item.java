package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Item {

    private static File inventoryFile = new File("vendingmachine.csv");

    public static Map<String, String> itemLocation = new HashMap<>();
    public static Map<String, String> itemName = new HashMap<>();
    public static Map<String, BigDecimal> itemPrice = new HashMap<>();
    public static Map<String, String> itemType = new HashMap<>();
    public static Map<String, Integer> itemStock = new HashMap<String, Integer>();
    public static int itemQuantity = 5;

    public static String[] itemArray = new String[4];

    public static void scanFile() {
        try (Scanner inventoryScanner = new Scanner(inventoryFile)) {
            while (inventoryScanner.hasNextLine()) {
                String itemLine = inventoryScanner.nextLine();
                itemArray = itemLine.split("\\|");
                itemLocation.put(itemArray[0], itemArray[0]);
                itemName.put(itemArray[0], itemArray[1]);
                itemPrice.put(itemArray[0], new BigDecimal(itemArray[2]));
                itemType.put(itemArray[0], itemArray[3]);
                itemStock.put(itemArray[0], itemQuantity);
            }
        } catch (FileNotFoundException fnfE) {
            System.out.println("File not found.");
        }
    }


}
