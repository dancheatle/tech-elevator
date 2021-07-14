package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransactionTest {

    @Before
    public void setup(){
        Item.scanFile();
    }


    @Test
    public void Transaction_setBalance_validValue() { //tests get and set
        Transaction trans = new Transaction();
        Transaction.setBalance(new BigDecimal("7.75"));
        BigDecimal expected = new BigDecimal("7.75");

        BigDecimal result = Transaction.getBalance();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Transaction_getBalance_negativeValue() { //tests get and set
        Transaction transTest = new Transaction();
        BigDecimal expected = new BigDecimal("-1.00");
        Transaction.setBalance(new BigDecimal("-1.00"));

        BigDecimal result = Transaction.getBalance();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Transaction_getBalance_nullValue() { //tests get and set
        Transaction trans = new Transaction();
        Transaction.setBalance(null);
        BigDecimal expected = null;

        BigDecimal result = Transaction.getBalance();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Transaction_dispenseChange_validValue() {
        Transaction trans = new Transaction();
        Transaction.setBalance(new BigDecimal("3.40"));
        String expected = "13 quarters, 1 dime, 1 nickel";

        String result = Transaction.dispenseChange();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Transaction_dispenseChange_zeroBalance() {
        Transaction trans = new Transaction();
        String expected = "";

        String result = Transaction.dispenseChange();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Transaction_dispenseChange_balanceResetAfterChangeGiven() {
        Transaction trans = new Transaction();
        Transaction.setBalance(new BigDecimal("3.40"));
        BigDecimal expected = new BigDecimal("0.00");

        Transaction.dispenseChange();
        BigDecimal result = Transaction.getBalance();

        Assert.assertEquals(expected, result);
    }
/*    @Test
    public void Transaction_purchaseItem_correctlyAffectsBalance() {
        Transaction trans = new Transaction();
        Transaction.setBalance(new BigDecimal("2.00"));

        VendingMachineCLI.userSelection = "D4";
        Transaction.purchaseItem();

        Assert.assertEquals(Transaction.getBalance(), new BigDecimal("1.25"));
    }*/


}
