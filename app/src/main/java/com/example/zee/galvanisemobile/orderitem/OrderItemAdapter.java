package com.example.zee.galvanisemobile.orderitem;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.zee.galvanisemobile.cart.CartActivity;
import com.example.zee.galvanisemobile.CustomLatteActivity;
import com.example.zee.galvanisemobile.R;
import com.example.zee.galvanisemobile.cart.ShoppingCart;
import com.example.zee.galvanisemobile.foodmenu.FoodItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    List<OrderItem> orderItems;
    private LayoutInflater inflater;
    private Context context;

    EditText quantity;
    OrderItem selectedItem;

    AQuery androidAQuery = new AQuery(context);

    public OrderItemAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        orderItems = ShoppingCart.getOrderItems();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.cart_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final FoodItem foodItem = orderItems.get(i).getFoodItem();

        viewHolder.itemName.setText(foodItem.getItemName());
        androidAQuery.id(viewHolder.imgThumbnail).image(foodItem.getThumbnail(), true, true);
        viewHolder.promoPrice.setText("$" + String.format("%.2f", foodItem.getPromoPrice()));
        viewHolder.quantityAdded.setText("Quantity added: " + orderItems.get(i).getQuantity());
        viewHolder.orderSubtotal.setText("Item Subtotal: $" + String.format("%.2f", orderItems.get(i).getQuantity()* foodItem.getPromoPrice()));
        viewHolder.itemView.setTag(orderItems.get(i));

        // check if this is a customizable food item
        if (foodItem.getcustomArtId() == null || foodItem.getcustomArtId().isEmpty()) {

            viewHolder.customArtLabel.setVisibility(View.GONE);

        } else {

            viewHolder.customArtLabel.setVisibility(View.VISIBLE);
            // allow thumnail and label to be clicked: to go edit custom latte art

            viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCustomArt(foodItem);
                }
            });

            viewHolder.customArtLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCustomArt(foodItem);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return orderItems.size();
    }

    private void editCustomArt(FoodItem menuItem) {

        Intent intent = new Intent(context, CustomLatteActivity.class);
        intent.putExtra("customFoodObject", menuItem);
        context.startActivity(intent);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public ImageView imgThumbnail;
        public TextView customArtLabel;
        public TextView itemName;
        public TextView promoPrice;
        public TextView quantityAdded;
        public TextView orderSubtotal;
        public TextView changeQuantity;
        public ImageView removeItem;

        public ViewHolder(final View itemView){
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            customArtLabel = (TextView)itemView.findViewById(R.id.custom_art_label);
            itemName = (TextView)itemView.findViewById(R.id.tv_menu_item_name);
            promoPrice = (TextView)itemView.findViewById(R.id.tv_menu_item_price);
            quantityAdded = (TextView)itemView.findViewById(R.id.tv_order_quantity);
            orderSubtotal = (TextView)itemView.findViewById(R.id.tv_order_subtotal);

            changeQuantity = (TextView)itemView.findViewById(R.id.change_quantity);
            changeQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderItem orderItem = (OrderItem)itemView.getTag();
                    selectedItem = orderItem;
                    updateInDialog(v);
                }
            });

            removeItem = (ImageView)itemView.findViewById(R.id.remove_icon);
            removeItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           delete(getLayoutPosition());
        }
    }

    // dialog pop-up to edit an item's quantity in the cart
    public void updateInDialog(View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.dialog_update_quantity);
        dialog.setTitle("Change Quantity");

        Button plusButton = (Button) dialog.findViewById(R.id.plus_button);
        Button minusButton = (Button) dialog.findViewById(R.id.minus_button);

        Button confirmAdd = (Button) dialog.findViewById(R.id.confirm_add);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        quantity = (EditText)dialog.findViewById(R.id.quantity);
        quantity.setText((String.valueOf(selectedItem.getQuantity())), TextView.BufferType.EDITABLE);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFromDialog = quantity.getText().toString();

                if (inputFromDialog.isEmpty()) {
                    quantity.setText(String.valueOf(1), TextView.BufferType.EDITABLE);
                } else {
                    int currValue = Integer.parseInt(inputFromDialog);
                    quantity.setText(String.valueOf(currValue+1), TextView.BufferType.EDITABLE);
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFromDialog = quantity.getText().toString();

                if (inputFromDialog.isEmpty()) {
                    quantity.setText(String.valueOf(1), TextView.BufferType.EDITABLE);
                } else {
                    int currValue = Integer.parseInt(inputFromDialog);
                    if (currValue <= 1) {
                        quantity.setText(String.valueOf(1), TextView.BufferType.EDITABLE);
                    } else {
                        quantity.setText(String.valueOf(currValue-1), TextView.BufferType.EDITABLE);
                    }
                }
            }
        });

        confirmAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String inputFromDialog = quantity.getText().toString();

                if (inputFromDialog.isEmpty() || Integer.parseInt(inputFromDialog) <= 0) {
                    alertEmptyItem();
                } else {
                    ShoppingCart.changeOrderQuantity(selectedItem, Integer.parseInt(inputFromDialog));
                    ((CartActivity) context).refreshCartInfo();

                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void alertEmptyItem() {
        Toast.makeText(context, "Minimum quantity should be 1.", Toast.LENGTH_SHORT).show();
    }

    // remove an existing item from the cart
    public void delete(int position) {
        OrderItem removed = orderItems.remove(position);
        ShoppingCart.removeItem(removed);
        ((CartActivity)context).refreshCartInfo();
        notifyItemRemoved(position);
    }

}
