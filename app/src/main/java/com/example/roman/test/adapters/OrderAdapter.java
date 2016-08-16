package com.example.roman.test.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> mOrders;
    private Context context;

    public OrderAdapter(Context context, List<Order> orders) {
        mOrders = orders;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_order, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Order order = mOrders.get(i);

        // Populate the data into the template view using the data object
        if (order != null) {
            viewHolder.from.setText(order.getFrom());
            viewHolder.to.setText(order.getTo());
            viewHolder.price.setText(context.getString(R.string.format_price,
                    String.valueOf(order.getPrice())));
        }
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_from)
        TextView from;

        @BindView(R.id.list_item_to)
        TextView to;

        @BindView(R.id.list_item_price)
        TextView price;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
