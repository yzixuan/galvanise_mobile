package com.example.zee.galvanisemobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zee on 20/10/15.
 */
public class ReceiptItem {

    private List<OrderItem> orderItems;
    private Date dateOfPurchase;
    private double subTotal;
    private double totalPaid;
    private double discount;

    public ReceiptItem(Date dateOfPurchase) {

        List<OrderItem> newList = new ArrayList<OrderItem>();
        newList.addAll(ShoppingCart.getOrderItems());

        this.orderItems = newList;
        this.dateOfPurchase = dateOfPurchase;
        this.subTotal = ShoppingCart.getTotalPrice();
        this.totalPaid = ShoppingCart.getDiscountedPrice();
        this.discount = ShoppingCart.getDiscount();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
