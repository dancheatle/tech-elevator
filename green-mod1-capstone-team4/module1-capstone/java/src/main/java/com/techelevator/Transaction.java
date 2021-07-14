package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Transaction {

    //instance variables

    private static BigDecimal balance = new BigDecimal("0.00");
    private static BigDecimal quarter = new BigDecimal("0.25");
    private static BigDecimal dime = new BigDecimal("0.10");
    private static BigDecimal nickel = new BigDecimal(".05");
    private static BigDecimal zero = new BigDecimal("0.00");

    private String transaction = "";
    private static BigDecimal previousBalance;


    //getters and setters


    public static BigDecimal getBalance() {
        return balance;
    }

    public static void setBalance(BigDecimal balance) {
        Transaction.balance = balance; }

    //methods


    public static String dispenseChange() {

        previousBalance = balance;

        BigDecimal sum = new BigDecimal("0.00");

        int quarterCount = 0;
        int dimeCount = 0;
        int nickelCount = 0;

        while (sum.compareTo(balance) < 0) {
            if ((balance.divide(quarter, 2, RoundingMode.CEILING).compareTo(zero) > 0 ) && sum.add(quarter).compareTo(balance) <= 0) {
                sum = sum.add(quarter);
                quarterCount += 1;
            }
            else if (balance.divide(dime, 2, RoundingMode.CEILING).compareTo(zero)  > 0 && sum.add(dime).compareTo(balance) <= 0) {
                sum = sum.add(dime);
                dimeCount += 1;
            }
            else if (balance.divide(nickel, 2, RoundingMode.CEILING).compareTo(zero) > 0 && sum.add(nickel).compareTo(balance) <= 0) {
                sum = sum.add(nickel);
                nickelCount += 1;
            }
        }

        String changeToDispense = "";

        if(quarterCount > 1) {
            changeToDispense += quarterCount + " quarters";
        } else if(quarterCount == 1) {
            changeToDispense += "1 quarter";
        }

        if(dimeCount > 1) {
            if(!changeToDispense.isEmpty()) { changeToDispense += ", "; }
            changeToDispense += dimeCount + " dimes";
        } else if(dimeCount == 1) {
            if(!changeToDispense.isEmpty()) { changeToDispense += ", "; }
            changeToDispense += "1 dime";
        }

        if(nickelCount > 1) {
            if(!changeToDispense.isEmpty()) { changeToDispense += ", "; }
            changeToDispense += nickelCount + " nickels";
        } else if(nickelCount == 1) {
            if(!changeToDispense.isEmpty()) { changeToDispense += ", "; }
            changeToDispense += "1 nickel";
        }

        if(balance.compareTo(zero) == 0) {
            System.out.println("***No change to dispense***");
        } else {
            System.out.println("Change dispensed: " + changeToDispense);
            balance = new BigDecimal("0.00");
        }

        writeToLog("GIVE CHANGE", previousBalance, balance);

        return changeToDispense;
    }

    public static void purchaseItem() {

        if (!Item.itemLocation.containsKey(VendingMachineCLI.userSelection)) {
            System.out.println("***Invalid product code, please select valid code***");
            VendingMachineCLI.displayPurchaseMenuOptions();

        }
        if ((Item.itemStock.get(VendingMachineCLI.userSelection) == 0)) {
            System.out.println("***Product is sold out, please make another selection***");
            VendingMachineCLI.displayPurchaseMenuOptions();
        }
        if (balance.compareTo(Item.itemPrice.get(VendingMachineCLI.userSelection)) < 0) {
            System.out.println("***Insufficient funds, please add money***");
            VendingMachineCLI.displayPurchaseMenuOptions();
        }

        previousBalance = balance;
        balance = balance.subtract(Item.itemPrice.get(VendingMachineCLI.userSelection));
        Item.itemStock.replace(VendingMachineCLI.userSelection, Item.itemStock.get(VendingMachineCLI.userSelection) - 1);
        System.out.println("Vending machine dispensed: " + Item.itemName.get(VendingMachineCLI.userSelection));
        System.out.println("The item cost: $" + Item.itemPrice.get(VendingMachineCLI.userSelection));
        System.out.println("Your remaining balance is: $" + balance);

        if (Item.itemType.get(VendingMachineCLI.userSelection).equals("Chip")) {
            System.out.println("Crunch Crunch, Yum!");
        }
        if (Item.itemType.get(VendingMachineCLI.userSelection).equals("Drink")) {
            System.out.println("Glug Glug, Yum!");
        }
        if (Item.itemType.get(VendingMachineCLI.userSelection).equals("Candy")) {
            System.out.println("Munch Munch, Yum!");
        }
        if (Item.itemType.get(VendingMachineCLI.userSelection).equals("Gum")) {
            System.out.println("Chew Chew, Yum!");
        }

        writeToLog(Item.itemName.get(VendingMachineCLI.userSelection) + " " + Item.itemLocation.get(VendingMachineCLI.userSelection), previousBalance, balance);
        VendingMachineCLI.displayPurchaseMenuOptions();
    }



    public static void writeToLog(String transaction, BigDecimal previousBalance, BigDecimal balance){

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        LocalDateTime dateTime = LocalDateTime.now();

        try(PrintWriter audit = new PrintWriter(new FileOutputStream(new File("Log.txt"), true))) {

            audit.println(dateTimeFormat.format(dateTime) + " " + transaction + " \\$" + previousBalance + " \\$" + balance);

        } catch(FileNotFoundException fnfE) {
            System.out.println("File not found.");
        }

    }
}



