package com.example.zee.galvanisemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    List<MenuItem> mItems;

    public MenuItemAdapter() {
        super();
        mItems = new ArrayList<>();
        MenuItem beverage = new MenuItem();
        beverage.setName("Matcha Iced Tea");
        beverage.setPromoPrice(3.50);
        beverage.setThumbnail(R.drawable.matcha);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Earl Grey Cream");
        beverage.setPromoPrice(4.50);
        beverage.setThumbnail(R.drawable.icecream);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Coconut Chia Tea");
        beverage.setPromoPrice(3.50);
        beverage.setThumbnail(R.drawable.coconut);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Butternut Rum");
        beverage.setPromoPrice(8.50);
        beverage.setThumbnail(R.drawable.butternut);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Melon Mojito");
        beverage.setPromoPrice(3.50);
        beverage.setThumbnail(R.drawable.mojito);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Georgia Peach");
        beverage.setPromoPrice(7.50);
        beverage.setThumbnail(R.drawable.georgiapeach);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Thai Red Tea");
        beverage.setPromoPrice(3.50);
        beverage.setThumbnail(R.drawable.thaipearl);
        mItems.add(beverage);

        beverage = new MenuItem();
        beverage.setName("Honey Latte");
        beverage.setPromoPrice(3.50);
        beverage.setThumbnail(R.drawable.creamy);
        mItems.add(beverage);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MenuItem beverage = mItems.get(i);
        viewHolder.tvbeverage.setText(beverage.getName());
        viewHolder.imgThumbnail.setImageResource(beverage.getThumbnail());
        viewHolder.promoPrice.setText("$" + String.format("%.2f", beverage.getPromoPrice()));
        viewHolder.itemView.setTag(beverage);
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvbeverage;
        public TextView promoPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvbeverage = (TextView)itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView)itemView.findViewById(R.id.tv_menu_item_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuItem food = (MenuItem)v.getTag();
                    Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
                    intent.putExtra("foodObject", food);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
