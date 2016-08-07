package com.example.roman.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    private static class ViewHolder {
        TextView from;
        TextView to;
//        TextView price;
    }

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, R.layout.list_item_air, orders);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Order order = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for now
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_air, parent, false);
            viewHolder.from = (TextView) convertView.findViewById(R.id.list_item_from);
            viewHolder.to = (TextView) convertView.findViewById(R.id.list_item_to);
//            viewHolder.price = (TextView) convertView.findViewById(R.id.list_item_price);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.to.setText(order.to);
        viewHolder.from.setText(order.from);
//        viewHolder.price.setText(order.price);

        // Return the completed view to render on screen
        return convertView;
    }
}
