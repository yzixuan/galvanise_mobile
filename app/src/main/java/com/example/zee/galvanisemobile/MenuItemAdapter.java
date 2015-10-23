package com.example.zee.galvanisemobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.customViewHolder> {

    private List<MenuItem> feedItemList;
    private Context mContext;


    public MenuItemAdapter(Context context, List<MenuItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public customViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_card_item, viewGroup, false);

        customViewHolder viewHolder = new customViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(customViewHolder viewHolder, int i) {
        MenuItem feedItem = feedItemList.get(i);

        //Download image using picasso library
        Picasso.with(mContext)
                .load(feedItem.getThumbnail())
                .tag(mContext)
                .into(viewHolder.imgThumbnail);

        //Setting text view title
        viewHolder.tvbeverage.setText(Html.fromHtml(feedItem.getItemName()));
        viewHolder.promoPrice.setText("$" + String.format("%.2f", feedItem.getPromoPrice()));
        viewHolder.itemDesc.setText(Html.fromHtml(feedItem.getItemDesc()));
        viewHolder.itemView.setTag(feedItem);
    }

    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void updateList(List<MenuItem> orders) {
        feedItemList = orders;
        notifyDataSetChanged();
    }

    public void filterList(int categoryId) {

        List<MenuItem> filteredList = new ArrayList<MenuItem>();
        Iterator<MenuItem> orderIterator = feedItemList.iterator();

        while (orderIterator.hasNext()) {
            MenuItem currOrder = orderIterator.next();
            if (currOrder.getCategory() == categoryId) {
                filteredList.add(currOrder);
            }
        }
        updateList(filteredList);
    }


    public class customViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvbeverage;
        public TextView promoPrice;
        public TextView itemDesc;


        public customViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvbeverage = (TextView) itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView) itemView.findViewById(R.id.tv_menu_item_price);
            itemDesc = (TextView) itemView.findViewById(R.id.tv_menu_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuItem food = (MenuItem) v.getTag();
                    Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
                    intent.putExtra("foodObject", food);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
