package com.example.zee.galvanisemobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zee on 8/10/15.
 */
public class MenuItem implements Parcelable{

    private int id;
    private int category;
    private String itemName;
    private double promoPrice;
    private String thumbnail;
    private String itemDesc;
    private boolean customizable = false;
    private String customArt;

    public MenuItem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        setCustomizableBasedOnId(id);
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCategoryViaName(String categoryName) {

        switch (categoryName) {
            case "Coffees":
                this.setCategory(1);
                break;
            case "Teas":
                this.setCategory(2);
                break;
            case "Juices":
                this.setCategory(3);
                break;
            case "Snacks":
                this.setCategory(4);
                break;
            case "Dreamy Desserts":
                this.setCategory(5);
                break;
            default:
                this.setCategory(0);
                break;
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc (String desc) {
        this.itemDesc = desc;
    }

    public boolean isCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

    public void setCustomizableBasedOnId(int id) {

        if (id == 20)
            this.customizable = true;
    }

    public String getCustomArt() {
        return customArt;
    }

    public void setCustomArt(String customArt) {
        this.customArt = customArt;
    }

    protected MenuItem(Parcel in) {
        id = in.readInt();
        category = in.readInt();
        itemName = in.readString();
        promoPrice = in.readDouble();
        thumbnail = in.readString();
        itemDesc = in.readString();
        customizable = in.readByte() != 0;
        customArt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(category);
        dest.writeString(itemName);
        dest.writeDouble(promoPrice);
        dest.writeString(thumbnail);
        dest.writeString(itemDesc);
        dest.writeByte((byte) (customizable ? 1 : 0));
        dest.writeString(customArt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

}
