package com.example.zee.galvanisemobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ShoppingCart implements Serializable {

    private static ArrayList<OrderItem> orderItems = new ArrayList<>();
    private static int numOfItems = 0;
    private static double totalPrice = 0;
    private static double discount = 0;
    private static double discountedPrice = totalPrice;

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
    }

    public static double getDiscountedPrice() {
        return discountedPrice;
    }

    // this is a private method
    private static void setDiscountedPrice() {
        discountedPrice = totalPrice * (1 - discount);
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
            if (currOrder.getMenuItem().getId() == toBeAdded.getMenuItem().getId()) {
                currOrder.setQuantity(currOrder.getQuantity() + toBeAdded.getQuantity());
                added = true;
            }
        }

        if (!added) {
            ShoppingCart.orderItems.add(toBeAdded);
        }

        updateCartTotal();
    }

    public static void clear() {
        ShoppingCart.orderItems.clear();
        ShoppingCart.totalPrice = 0;
    }

    private static void updateCartTotal() {

        int tempQuantity = 0;
        double tempPrice = 0;

        Iterator<OrderItem> orderIterator = orderItems.iterator();
        while (orderIterator.hasNext()) {

            OrderItem currOrder = orderIterator.next();

            tempQuantity += currOrder.getQuantity();
//            tempPrice += currOrder.getMenuItem().getPromoPrice() * currOrder.getQuantity();
        }

        numOfItems = tempQuantity;
        totalPrice = tempPrice;
        setDiscountedPrice();
    }

    public static void removeItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        updateCartTotal();
    }
}
