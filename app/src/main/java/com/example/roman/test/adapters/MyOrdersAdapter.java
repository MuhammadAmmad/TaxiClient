package com.example.roman.test.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.Record;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersAdapter extends ArrayAdapter<Record> {
    static class ViewHolder {

        @BindView(R.id.list_item_time)
        TextView time;

        @BindView(R.id.list_item_from)
        TextView from;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MyOrdersAdapter(Context context, List<Record> orders) {
        super(context, R.layout.list_item_my_order, orders);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Record order = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder;

        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for now
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_my_order, parent, false);

            holder = new ViewHolder(view);
            // Cache the viewHolder object inside the fresh view
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.from.setText("");
            holder.time.setText("");
        }

        if (order != null) {
            holder.from.setText(order.getFromAddress());
            holder.time.setText(order.getTime());
        }

        return view;
    }
}
