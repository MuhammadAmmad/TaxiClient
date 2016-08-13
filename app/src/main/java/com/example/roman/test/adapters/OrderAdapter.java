package com.example.roman.test.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roman.test.data.Order;
import com.example.roman.test.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends ArrayAdapter<Order> {

    static class ViewHolder {
        @BindView(R.id.list_item_from)
        TextView from;

        @BindView(R.id.list_item_to)
        TextView to;

        @BindView(R.id.list_item_price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, R.layout.list_item_air, orders);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Order order = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder;

        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for now
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_air, parent, false);

            holder = new ViewHolder(view);

            // Cache the viewHolder object inside the fresh view
            view.setTag(holder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            holder = (ViewHolder) view.getTag();
        }

        // Populate the data into the template view using the data object
        if (order != null) {
            holder.to.setText(order.getTo());
            holder.from.setText(order.getFrom());
            holder.price.setText(getContext()
                    .getString(R.string.format_price, String.valueOf(order.getPrice())));
        }

        // Return the completed view to render on screen
        return view;
    }
}
