package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ItemTest {

    @Before
    public void setup(){
        Item.scanFile();
    }

    @Test
    public void Item_itemLocation_codeValue_isCorrect(){
        Item codeTest = new Item();
        String expected = "A1";

        String result = Item.itemLocation.get("A1");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Item_itemName_nameValue_isCorrect(){
        Item codeTest = new Item();
        String expected = "Heavy";

        String result = Item.itemName.get("C4");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Item_itemPrice_priceValue_isCorrect(){
        Item codeTest = new Item();
        BigDecimal expected = new BigDecimal("0.85");

        BigDecimal result = Item.itemPrice.get("D1");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Item_itemType_typeValue_isCorrect(){
        Item codeTest = new Item();
        String expected = "Candy";

        String result = Item.itemType.get("B3");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void Item_itemStock_stockValue_isCorrect(){
        Item codeTest = new Item();
        int expected = 5;

        int result = Item.itemStock.get("B2");

        Assert.assertEquals(expected, result);
    }




}



