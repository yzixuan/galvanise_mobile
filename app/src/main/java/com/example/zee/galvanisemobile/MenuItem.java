package com.example.zee.galvanisemobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zee on 8/10/15.
 */
public class MenuItem implements Parcelable{
    private String mName;
    private double mPromoPrice;
    private int mThumbnail;

    public MenuItem(){

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getPromoPrice() {
        return mPromoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.mPromoPrice = promoPrice;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.mThumbnail = thumbnail;
    }

    protected MenuItem(Parcel in) {
        mName = in.readString();
        mPromoPrice = in.readDouble();
        mThumbnail = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mPromoPrice);
        dest.writeInt(mThumbnail);
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
