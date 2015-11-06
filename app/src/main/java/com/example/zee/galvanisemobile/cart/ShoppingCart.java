package com.example.zee.galvanisemobile.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.example.zee.galvanisemobile.estimote.CafeBeacon;
import com.example.zee.galvanisemobile.orderitem.OrderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ShoppingCart implements Serializable {

    private static ArrayList<OrderItem> orderItems = new ArrayList<>();
    private static int numOfItems = 0;
    private static double totalPrice = 0;
    private static double discount = 0;
    private static double discountedPrice = totalPrice;
    private static String tableNumber = null;
    private static Context context;
    private static SharedPreferences sharedPreferences;
    private static boolean loadedFromPreferences = false;

    public ShoppingCart(Context ctx) {
        context = ctx;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        if (!loadedFromPreferences) {
            loadFromPreferences();
            loadedFromPreferences = true;
        }
    }

    public static int getNumOfItems() {
        return numOfItems;
    }

    public static void setNumOfItems(int numOfItems) {
        ShoppingCart.numOfItems = numOfItems;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }

    public static void setTotalPrice(double totalPrice) {
        ShoppingCart.totalPrice = totalPrice;
    }

    public static double getDiscount() {
        return discount;
    }

    public static void setDiscount(double discount) {
        ShoppingCart.discount = discount;
        setDiscountedPrice();
        saveToPreferences();
    }

    public static double getDiscountedPrice() {
        return discountedPrice;
    }

    private static void setDiscountedPrice() {
        discountedPrice = totalPrice * (1 - discount);
    }

    public static void setDiscountedPrice(double discountedPrice) {
        ShoppingCart.discountedPrice = discountedPrice;
    }

    public static String getTableNumber() {
        return tableNumber;
    }

    public static void setTableNumber(String tableNumber) {
        ShoppingCart.tableNumber = tableNumber;
        saveToPreferences();
    }

    public static ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public static void setOrderItems(ArrayList<OrderItem> orderItems) {
        ShoppingCart.orderItems = orderItems;
    }

    public static void addOrderItem(OrderItem toBeAdded) {

        boolean added = false;

        Iterator<OrderItem> orderIterator = orderItems.iterator();
        while (orderIterator.hasNext()) {
            OrderItem currOrder = orderIterator.next();

            if (currOrder.getFoodItem().getId() == toBeAdded.getFoodItem().getId() &&
                    currOrder.getFoodItem().getcustomArtId().equals(toBeAdded.getFoodItem().getcustomArtId())) {

                currOrder.setQuantity(currOrder.getQuantity() + toBeAdded.getQuantity());
                added = true;

            }
        }

        if (!added) {
            ShoppingCart.orderItems.add(toBeAdded);
        }

        updateCartTotal();
    }

    public static void changeOrderQuantity(OrderItem orderItem, int quantity) {

        Iterator<OrderItem> orderIterator = orderItems.iterator();

        while (orderIterator.hasNext()) {

            OrderItem currOrder = orderIterator.next();
            if (currOrder.getFoodItem().getId() == orderItem.getFoodItem().getId() &&
                    currOrder.getFoodItem().getcustomArtId().equals(orderItem.getFoodItem().getcustomArtId())) {

                currOrder.setQuantity(quantity);
            }
        }

        updateCartTotal();
    }

    public static void clear() {
        tableNumber = null;
        orderItems.clear();
        setDiscount(0);
        CafeBeacon.setPromoDiscountClicked(false);
        updateCartTotal();
    }

    private static void updateCartTotal() {

        int tempQuantity = 0;
        double tempPrice = 0;

        Iterator<OrderItem> orderIterator = orderItems.iterator();
        while (orderIterator.hasNext()) {

            OrderItem currOrder = orderIterator.next();

            if (currOrder != null) {
                tempQuantity += currOrder.getQuantity();
                tempPrice += currOrder.getFoodItem().getPromoPrice() * currOrder.getQuantity();
            }

        }

        numOfItems = tempQuantity;
        totalPrice = tempPrice;
        setDiscountedPrice();
        saveToPreferences();
    }

    public static void removeItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        updateCartTotal();
    }

    // save shopping cart data to shared preferences to persist it
    // so users can still access their cart items even after quiting the app or rebooting device
    public static void saveToPreferences() {

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        prefsEditor.putString("CartTableNumber", tableNumber);
        prefsEditor.putFloat("CartDiscount", (float) discount);
        prefsEditor.putFloat("CartTotalPrice", (float) totalPrice);
        prefsEditor.putFloat("CartDiscountedPrice", (float) discountedPrice);
        prefsEditor.putInt("CartNumOfItems", numOfItems);
        prefsEditor.putInt("CartNumOfTypes", orderItems.size());

        Gson gson = new Gson();
        for(int i = 0; i < orderItems.size(); i++) {
            String json = gson.toJson(orderItems.get(i));
            prefsEditor.putString("CartOrderItems_" + i, json);
        }

        prefsEditor.apply();
    }

    // load any saved shopping cart data from shared preferences
    public void loadFromPreferences() {

        tableNumber = sharedPreferences.getString("CartTableNumber", null);
        discount = sharedPreferences.getFloat("CartDiscount", 0);
        totalPrice = sharedPreferences.getFloat("CartTotalPrice", 0);
        discountedPrice =  sharedPreferences.getFloat("CartDiscountedPrice", 0);
        numOfItems = sharedPreferences.getInt("CartNumOfItems", 0);

        int numOfTypes = sharedPreferences.getInt("CartNumOfTypes", 0);

        for(int i = 0; i < numOfTypes; i++) {
            String json = sharedPreferences.getString("CartOrderItems_" + i, "");
            Gson gson = new Gson();
            OrderItem orderItem = gson.fromJson(json, OrderItem.class);
            orderItems.add(orderItem);
        }
    }
}
