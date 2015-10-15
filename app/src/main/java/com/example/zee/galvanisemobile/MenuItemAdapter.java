package com.example.zee.galvanisemobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.customViewHolder> {

    //    List<MenuItem> mItems;
    private List<MenuItem> feedItemList;
    private Context mContext;

    public MenuItemAdapter(Context context, List<MenuItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

//    public MenuItemAdapter() {
//        super();
//        mItems = new ArrayList<>();
//        MenuItem beverage = new MenuItem();
//        beverage.setId(1);
//        beverage.setItemName("Matcha Iced Tea");
//        beverage.setPromoPrice(3.50);
//        beverage.setThumbnail(R.drawable.matcha);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(2);
//        beverage.setItemName("Earl Grey Cream");
//        beverage.setPromoPrice(4.50);
//        beverage.setThumbnail(R.drawable.icecream);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(3);
//        beverage.setItemName("Coconut Chia Tea");
//        beverage.setPromoPrice(3.50);
//        beverage.setThumbnail(R.drawable.coconut);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(4);
//        beverage.setItemName("Butternut Rum");
//        beverage.setPromoPrice(8.50);
//        beverage.setThumbnail(R.drawable.butternut);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(5);
//        beverage.setItemName("Melon Mojito");
//        beverage.setPromoPrice(3.50);
//        beverage.setThumbnail(R.drawable.mojito);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(6);
//        beverage.setItemName("Georgia Peach");
//        beverage.setPromoPrice(7.50);
//        beverage.setThumbnail(R.drawable.georgiapeach);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(7);
//        beverage.setItemName("Thai Red Tea");
//        beverage.setPromoPrice(3.50);
//        beverage.setThumbnail(R.drawable.thaipearl);
//        mItems.add(beverage);
//
//        beverage = new MenuItem();
//        beverage.setId(8);
//        beverage.setItemName("Honey Latte");
//        beverage.setPromoPrice(3.50);
//        beverage.setThumbnail(R.drawable.creamy);
//        mItems.add(beverage);
//    }

//    @Override
//    public customViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.menu_card_item, viewGroup, false);
//        RecyclerView.ViewHolder viewHolder = new customViewHolder(v);
//        return viewHolder;
//    }

    @Override
    public customViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_card_item, viewGroup, false);

        customViewHolder viewHolder = new customViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(customViewHolder viewHolder, int i) {
//        MenuItem beverage = mItems.get(i);
//        viewHolder.tvbeverage.setText(beverage.getItemName());
//        viewHolder.imgThumbnail.setImageResource(beverage.getThumbnail());
//        viewHolder.promoPrice.setText("$" + String.format("%.2f", beverage.getPromoPrice()));
//        viewHolder.itemView.setTag(beverage);
//    }
        MenuItem feedItem = feedItemList.get(i);


        //Download image using picasso library
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(RecyclerView.ViewHolder.imgThumbnail);

        //Setting text view title
        customViewHolder.tvbeverage.setText(Html.fromHtml(feedItem.getItemName()));

    }

    //    @Override
//    public int getItemCount() {
//
//        return mItems.size();
//    }
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class customViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvbeverage;
        public TextView promoPrice;

        public customViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvbeverage = (TextView) itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView) itemView.findViewById(R.id.tv_menu_item_price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MenuItem food = (MenuItem) v.getTag();
                    customViewHolder holder = (customViewHolder) v.getTag();
                    int position = holder.getPosition();

                    MenuItem feedItem = feedItemList.get(position);
                    Toast.makeText(mContext, feedItem.getItemName(), Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
//                    intent.putExtra("foodObject", food);
//                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
