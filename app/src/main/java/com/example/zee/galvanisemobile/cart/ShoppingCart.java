package com.example.zee.galvanisemobile.cart;

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

    public static void setDiscountedPrice(double discountedPrice) {
        ShoppingCart.discountedPrice = discountedPrice;
    }

    public static String getTableNumber() {
        return tableNumber;
    }

    public static void setTableNumber(String tableNumber) {
        ShoppingCart.tableNumber = tableNumber;
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
            if (currOrder.getFoodItem().getId() == toBeAdded.getFoodItem().getId()) {

                // if it's non-customizable, or custom art id matches, add to existing order
                if (currOrder.getFoodItem().getcustomArtId() == null ||
                    currOrder.getFoodItem().getcustomArtId().equals(toBeAdded.getFoodItem().getcustomArtId()) ) {

                    currOrder.setQuantity(currOrder.getQuantity() + toBeAdded.getQuantity());
                    added = true;
                }
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
            if (currOrder.getFoodItem().getId() == orderItem.getFoodItem().getId()) {
                currOrder.setQuantity(quantity);
            }
        }

        updateCartTotal();
    }

    public static void clear() {
        tableNumber = null;
        orderItems.clear();
        updateCartTotal();
    }

    private static void updateCartTotal() {

        int tempQuantity = 0;
        double tempPrice = 0;

        Iterator<OrderItem> orderIterator = orderItems.iterator();
        while (orderIterator.hasNext()) {

            OrderItem currOrder = orderIterator.next();

            tempQuantity += currOrder.getQuantity();
            tempPrice += currOrder.getFoodItem().getPromoPrice() * currOrder.getQuantity();
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