package com.example.roman.test.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roman.test.AirFragment;
import com.example.roman.test.R;
import com.example.roman.test.data.Order;
import com.example.roman.test.utilities.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.utilities.Constants.NEW_FORMAT;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> mOrders;
    private Context context;
    private AirFragment mAirFragment;
    private final View.OnClickListener mOnClickListener;

    public OrderAdapter(Context context, List<Order> orders, AirFragment airFragment) {
        mOrders = orders;
        this.context = context;
        mAirFragment = airFragment;
        mOnClickListener = airFragment.new MyOnClickListener();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_order, viewGroup, false);
        view.setOnClickListener(mOnClickListener);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Order order = mOrders.get(i);

        // Populate the data into the template view using the data object
        if (order != null) {
            viewHolder.from.setText(order.getFrom());
            viewHolder.description.setText(order.getDescription());
            viewHolder.time.setText(order.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_from)
        TextView from;

        @BindView(R.id.list_item_description)
        TextView description;

        @BindView(R.id.list_item_time)
        TextView time;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
