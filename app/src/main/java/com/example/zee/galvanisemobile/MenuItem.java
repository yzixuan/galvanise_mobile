package com.example.zee.galvanisemobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zee on 8/10/15.
 */
public class MenuItem implements Parcelable {

    private int id;
    private int category_id;
    private String itemName;
    private double promoPrice;
    private int thumbnail;

    public MenuItem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    protected MenuItem(Parcel in) {
        id = in.readInt();
        category_id = in.readInt();
        itemName = in.readString();
        promoPrice = in.readDouble();
        thumbnail = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(category_id);
        dest.writeString(itemName);
        dest.writeDouble(promoPrice);
        dest.writeInt(thumbnail);
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
