package com.example.zee.galvanisemobile;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zee on 10/10/15.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    List<OrderItem> menuitems;

    public OrderItemAdapter() {
        super();
        menuitems = ShoppingCart.getOrderItems();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MenuItem menuItem = menuitems.get(i).getMenuItem();
        viewHolder.itemName.setText(menuItem.getItemName());
        viewHolder.imgThumbnail.setImageResource(menuItem.getThumbnail());
        viewHolder.promoPrice.setText("$" + String.format("%.2f", menuItem.getPromoPrice()));
        viewHolder.quantityAdded.setText("Quantity added: " + menuitems.get(i).getQuantity());
        viewHolder.orderSubtotal.setText("Item Subtotal: $" + String.format("%.2f", menuitems.get(i).getQuantity()* menuItem.getPromoPrice()));
        viewHolder.itemView.setTag(menuitems.get(i));
    }

    @Override
    public int getItemCount() {

        return menuitems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView itemName;
        public TextView promoPrice;
        public TextView quantityAdded;
        public TextView orderSubtotal;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            itemName = (TextView)itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView)itemView.findViewById(R.id.tv_menu_item_price);
            quantityAdded = (TextView)itemView.findViewById(R.id.tv_order_quantity);
            orderSubtotal = (TextView)itemView.findViewById(R.id.tv_order_subtotal);
        }
    }

}
