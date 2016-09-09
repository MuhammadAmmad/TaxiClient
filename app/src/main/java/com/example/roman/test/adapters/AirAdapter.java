package com.example.roman.test.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AirAdapter extends ArrayAdapter<Order> {
    static class ViewHolder {

        @BindView(R.id.list_item_from)
        TextView from;

        @BindView(R.id.list_item_description)
        TextView description;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public AirAdapter(Context context, List<Order> orders) {
        super(context, R.layout.list_item_air_order, orders);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Order order = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        AirAdapter.ViewHolder holder;

        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for now
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_air_order, parent, false);

            holder = new AirAdapter.ViewHolder(view);
            // Cache the viewHolder object inside the fresh view
            view.setTag(holder);
        } else {
            holder = (AirAdapter.ViewHolder) view.getTag();
            holder.from.setText("");
            holder.description.setText("");
        }

        if (order != null) {
            holder.from.setText(order.getFrom());
            holder.description.setText(order.getDescription());
        }

        return view;
    }
}
