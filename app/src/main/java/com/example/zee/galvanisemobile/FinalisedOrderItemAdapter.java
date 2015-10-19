package com.example.zee.galvanisemobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

/**
 * Created by zee on 19/10/15.
 */
public class FinalisedOrderItemAdapter extends RecyclerView.Adapter<FinalisedOrderItemAdapter.ViewHolder> {

    List<OrderItem> menuitems;
    private LayoutInflater inflater;
    private Context context;

    AQuery androidAQuery = new AQuery(context);

    public FinalisedOrderItemAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        menuitems = ShoppingCart.getOrderItems();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.finalised_cart__card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MenuItem menuItem = menuitems.get(i).getMenuItem();
        viewHolder.itemName.setText(menuItem.getItemName());
        androidAQuery.id(viewHolder.imgThumbnail).image(menuItem.getThumbnail(), true, true);

        String promoPrice = String.format("%.2f", menuItem.getPromoPrice());
        String itemSubtotal = String.format("%.2f", menuitems.get(i).getQuantity() * menuItem.getPromoPrice());

        viewHolder.promoPrice.setText("$" + promoPrice + " x " + menuitems.get(i).getQuantity() + " = SGD $" + itemSubtotal);
        viewHolder.itemView.setTag(menuitems.get(i));
    }

    public void updateList(List<OrderItem> orders) {
        menuitems = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return menuitems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView itemName;
        public TextView promoPrice;

        public ViewHolder(final View itemView){
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            itemName = (TextView)itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView)itemView.findViewById(R.id.tv_menu_item_price);
        }

    }

}
