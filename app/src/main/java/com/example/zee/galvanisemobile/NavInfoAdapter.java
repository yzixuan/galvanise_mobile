package com.example.zee.galvanisemobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by zee on 8/10/15.
 */
public class NavInfoAdapter extends RecyclerView.Adapter<NavInfoAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    List<NavInfo> data = Collections.emptyList();

    public NavInfoAdapter(Context context, List<NavInfo> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavInfo current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.nav_list_text);
            icon = (ImageView) itemView.findViewById(R.id.nav_list_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent;

            switch (getLayoutPosition()) {
                case 0: // cafe menu
                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                    break;
                case 1: // your order
                    v.getContext().startActivity(new Intent(v.getContext(), CartActivity.class));
                    break;
                case 3: // advance booking
                    v.getContext().startActivity(new Intent(v.getContext(), BookingActivity.class));
                    break;
                case 4: // about us
                    v.getContext().startActivity(new Intent(v.getContext(), AboutActivity.class));
                    break;
                case 5: // chat with a staff
                    v.getContext().startActivity(new Intent(v.getContext(), ChatActivity.class));
                    break;
                case 6: // settings
                    v.getContext().startActivity(new Intent(v.getContext(), SettingsActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
