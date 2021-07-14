package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };
	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION };

	private static Menu menu;
	private Object Scanner;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() throws FileNotFoundException {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				System.out.println("\n*****Current Stock*****\n");
				displayVendingMachineOptions();
				System.out.println();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				displayPurchaseMenuOptions();


			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)){
				System.out.println("***Thank you, come again!***");
				System.exit(0);

			}
		}
	}

	public static String userDepositString = "";
	public static BigDecimal userDepositInput;
	public static String userSelection = "";

	public static void displayPurchaseMenuOptions() {

		System.out.println("\nCurrent Money Provided: $" + Transaction.getBalance());

		String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

		if(choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
			Scanner userDepositScanner = new Scanner(System.in);

			System.out.println("\n***This machine only accepts $1.00, $2.00, $5.00, and $10.00 bills***");
			System.out.print("Please enter money in proper format (ex. 1.00): ");

			userDepositString = userDepositScanner.nextLine();
			userDepositInput = new BigDecimal(userDepositString);

			if (!(userDepositString.equals("1.00") || userDepositString.equals("2.00") || userDepositString.equals("5.00") || userDepositString.equals("10.00"))){
				System.out.println("***Enter valid amount in proper format (ex. 1.00)***");
				displayPurchaseMenuOptions();
			}

			Transaction.setBalance(userDepositInput.add(Transaction.getBalance()));
			Transaction.writeToLog("FEED MONEY", VendingMachineCLI.userDepositInput, Transaction.getBalance());
			displayPurchaseMenuOptions();

		} else if(choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
			try{
				System.out.println("\n*****Current Stock*****\n");
				 displayVendingMachineOptions();
				System.out.println();
			}
			catch(FileNotFoundException e){
				System.out.println("File Not Found!");
			}

			Scanner userPurchaseScanner = new Scanner(System.in);

			System.out.print("Please enter item slot location (ex. A1): ");

			userSelection = userPurchaseScanner.nextLine();
			System.out.println();
			Transaction.purchaseItem();
		}
		else if(choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)){
			Transaction.dispenseChange();
			try {
				Menu menu = new Menu(System.in, System.out);
				VendingMachineCLI cli = new VendingMachineCLI(menu);
				cli.run();
			} catch(FileNotFoundException fnfEx) {
				System.out.println("No file found");
			}
		}
	}

	public static void displayVendingMachineOptions() throws FileNotFoundException {
		File inventoryFile = new File("vendingmachine.csv");
		try (java.util.Scanner inventoryScanner = new Scanner(inventoryFile)) {
			while (inventoryScanner.hasNextLine()) {
				String lineToSearch = inventoryScanner.nextLine();
				String codeToSearch = lineToSearch.substring(0, 2);

				  if(Item.itemStock.get(codeToSearch) == 0 ) {
					System.out.println(lineToSearch + "|" + "SOLD OUT");
				  } else {
				  	System.out.println(lineToSearch + "|" + Item.itemStock.get(codeToSearch));
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException  {
		System.out.println();
		System.out.println("*****Vendo-Matic 800*****");
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		Item.scanFile();

		cli.run();
	}
}
