package com.example.roman.test.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.Record;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.utilities.Constants.SHOW_ADDRESS_END;
import static com.example.roman.test.utilities.Constants.SHOW_ADDRESS_START;
import static com.example.roman.test.utilities.Constants.SHOW_DATE_START;
import static com.example.roman.test.utilities.Constants.SHOW_DESCRIPTION;
import static com.example.roman.test.utilities.Constants.SHOW_OPTIONS;
import static com.example.roman.test.utilities.Constants.SHOW_PHONE_NUMBER;
import static com.example.roman.test.utilities.Constants.SHOW_PRICE;
import static com.example.roman.test.utilities.Constants.SHOW_ROUTE_LENGTH;
import static com.example.roman.test.utilities.Constants.SHOW_TARIFF;
import static com.example.roman.test.utilities.Constants.SHOW_TIME_CREATED;
import static com.example.roman.test.utilities.Constants.SHOW_TIME_START;
import static com.example.roman.test.utilities.Functions.showField;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<Record> mOrders;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_date_created)
        TextView dateCreated;

        @BindView(R.id.order_time_created)
        TextView timeCreated;

        @BindView(R.id.order_phone)
        TextView phone;

        @BindView(R.id.order_description)
        TextView description;

        @BindView(R.id.order_price)
        TextView price;

        @BindView(R.id.order_options)
        TextView options;

        @BindView(R.id.order_from)
        TextView from;

        @BindView(R.id.order_to)
        TextView to;

        @BindView(R.id.order_tariff_id)
        TextView tariffId;

        @BindView(R.id.order_date)
        TextView date;

        @BindView(R.id.order_time)
        TextView time;

        @BindView(R.id.order_distance)
        TextView distance;

        @BindView(R.id.order_sector)
        TextView sector;

        @BindView(R.id.action_map)
        Button map;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public OrderAdapter(Context context) {
        this.context = context;
        mOrders = new ArrayList<>();
        Record first = new Record();
        Record second = new Record();

//        new GetOrdersTask().execute();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record order = null;
        if (mOrders != null) {
            order = mOrders.get(position);
        }

        if (order != null) {
//            int mask = Integer.parseInt(Functions
//                    .getFromPreferences(NEW_ORDER_MASK, prefs));
            int mask = 4096;

            if (showField(mask, SHOW_TIME_CREATED) && !order.getTimeCreated().equals("")) {
                holder.timeCreated.setVisibility(View.VISIBLE);
                holder.timeCreated.setText(context.getString(R.string.format_order_time_created,
                        order.getTimeCreated()));
            }

            if (showField(mask, SHOW_PHONE_NUMBER) && !order.getPhone().equals("")) {
                holder.phone.setVisibility(View.VISIBLE);
                holder.phone.setText(context.getString(R.string.format_order_phone, order.getPhone()));
            }

            if (showField(mask, SHOW_DESCRIPTION) && !order.getDescription().equals("")) {
                holder.description.setVisibility(View.VISIBLE);
                holder.description.setText(context.getString(R.string.format_order_description, order.getDescription()));
            }

            if (showField(mask, SHOW_PRICE) && !order.getPrice().equals("")) {
                holder.price.setVisibility(View.VISIBLE);
                holder.price.setText(context.getString(R.string.format_order_price, order.getPrice().split(",")[0]));
            }

            if (showField(mask, SHOW_OPTIONS) && !order.getOption().equals("0")) {
                holder.options.setVisibility(View.VISIBLE);
                holder.options.setText(context.getString(R.string.format_order_options, order.getOption()));
            }

            if (showField(mask, SHOW_ADDRESS_START) && !order.getFromAddress().equals("")) {
                holder.from.setVisibility(View.VISIBLE);
                holder.from.setText(context.getString(R.string.format_order_from, order.getFromAddress()));
            }

            if (showField(mask, SHOW_ADDRESS_END) && !order.getToAddress().equals("")) {
                holder.to.setVisibility(View.VISIBLE);
                holder.to.setText(context.getString(R.string.format_order_to, order.getToAddress()));
            }

            if (showField(mask, SHOW_DATE_START) && !order.getDate().equals("")) {
                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(context.getString(R.string.format_order_date, order.getDate()));
            }

            if (showField(mask, SHOW_TIME_START) && !order.getTime().equals("")) {
                holder.time.setVisibility(View.VISIBLE);
                holder.time.setText(context.getString(R.string.format_order_time, order.getTime()));
            }

            if (showField(mask, SHOW_ROUTE_LENGTH) && !order.getLength().equals("")) {
                holder.distance.setVisibility(View.VISIBLE);
                holder.distance.setText(context.getString(R.string.format_order_distance, order.getLength()));
            }

            if (showField(mask, SHOW_TARIFF) && !order.getTariffId().equals("")) {
                holder.tariffId.setVisibility(View.VISIBLE);
                holder.tariffId.setText(context.getString(R.string.format_order_tariff, order.getTariffId()));
            }
        }
    }

    @Override
    public int getItemCount() {
//        return mOrders.size();
        return 0;
    }

    private class GetOrdersTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
//            Cursor cursor = context.getContentResolver()
//                    .query(OrdersTable.CONTENT_URI, null, null, null, null);
//
//            if (cursor != null) {
//                mOrders = OrdersTable.getRows(cursor, true);
//            }
            return null;
        }
    }
}
