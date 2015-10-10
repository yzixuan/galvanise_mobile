package com.example.zee.galvanisemobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ShoppingCart implements Serializable {

    private static ArrayList<OrderItem> orderItems = new ArrayList<>();
    private static int numOfItems = 0;
    private static double totalPrice = 0;

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

    public static ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public static void setOrderItems(ArrayList<OrderItem> orderItems) {
        ShoppingCart.orderItems = orderItems;
    }

    public static void addOrderItem(OrderItem orderItem) {
        ShoppingCart.orderItems.add(orderItem);
        updateCartInfo();
    }

    public static void clear() {
        ShoppingCart.orderItems.clear();
        ShoppingCart.totalPrice = 0;
    }

    private static void updateCartInfo() {

        int tempQuantity = 0;
        double tempPrice = 0;

        Iterator<OrderItem> orderIterator = orderItems.iterator();
        while (orderIterator.hasNext()) {

            OrderItem currOrder = orderIterator.next();

            tempQuantity += currOrder.getQuantity();
            tempPrice += currOrder.getMenuItem().getPromoPrice() * currOrder.getQuantity();
        }

        ShoppingCart.numOfItems = tempQuantity;
        ShoppingCart.totalPrice = tempPrice;

    }
}
