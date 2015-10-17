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
 * Created by zee on 10/10/15.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    List<OrderItem> menuitems;
    private LayoutInflater inflater;
    private Context context;

    AQuery androidAQuery = new AQuery(context);

    public OrderItemAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        menuitems = ShoppingCart.getOrderItems();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.cart_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MenuItem menuItem = menuitems.get(i).getMenuItem();
        viewHolder.itemName.setText(menuItem.getItemName());
        androidAQuery.id(viewHolder.imgThumbnail).image(menuItem.getThumbnail(), true, true);
        viewHolder.promoPrice.setText("$" + String.format("%.2f", menuItem.getPromoPrice()));
        viewHolder.quantityAdded.setText("Quantity added: " + menuitems.get(i).getQuantity());
        viewHolder.orderSubtotal.setText("Item Subtotal: $" + String.format("%.2f", menuitems.get(i).getQuantity()* menuItem.getPromoPrice()));
        viewHolder.itemView.setTag(menuitems.get(i));
    }

    @Override
    public int getItemCount() {

        return menuitems.size();
    }

    public void delete(int position) {
        OrderItem removed = menuitems.remove(position);
        ShoppingCart.removeItem(removed);
        ((CartActivity)context).refreshCartInfo();
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public ImageView imgThumbnail;
        public TextView itemName;
        public TextView promoPrice;
        public TextView quantityAdded;
        public TextView orderSubtotal;
        public ImageView removeItem;

        public ViewHolder(View itemView){
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            itemName = (TextView)itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView)itemView.findViewById(R.id.tv_menu_item_price);
            quantityAdded = (TextView)itemView.findViewById(R.id.tv_order_quantity);
            orderSubtotal = (TextView)itemView.findViewById(R.id.tv_order_subtotal);

            removeItem = (ImageView)itemView.findViewById(R.id.remove_icon);
            removeItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           delete(getLayoutPosition());
        }
    }

}
