package com.iz;

import com.iz.model.PriceClass;
import org.mvel2.MVEL;

import java.math.BigDecimal;

public class MvelMain {
    public static void main1(String[] args) {
        PriceClass property = new PriceClass();
//        property.setOriginalPrice(new BigDecimal("100000.00"));
//        property.setCurrentPrice(new BigDecimal("95000.00"));

        // MVEL expression to calculate the price difference and set it to the priceDifference property
        String expression = "priceDifference = currentPrice - originalPrice; " +
                "status = priceDifference > 0 ? 'UnderCharged' : " +
                "(priceDifference < 0 ? 'OverCharged' : 'NoChange')";

        // Evaluate the expression using MVEL
        MVEL.eval(expression, property);
//        System.out.println("Price Difference: " + property.getPriceDifference());
//        System.out.println("Status: " + property.getStatus());
    }
}
