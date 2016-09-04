package com.example.roman.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roman.test.data.Order;
import com.example.roman.test.utilities.Functions;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.utilities.Constants.NEW_ORDER_MASK;
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

public class OrderFragment extends Fragment {
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

    @Inject SharedPreferences prefs;
    @Inject Gson gson;

    static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    public void addOrder(Order order) {
        Context context = getContext();

        if (order != null) {
            int mask = Integer.parseInt(Functions
                    .getFromPreferences(NEW_ORDER_MASK, prefs));

            if (showField(mask, SHOW_TIME_CREATED) && !order.getTimeCreated().equals("")) {
                timeCreated.setVisibility(View.VISIBLE);
                timeCreated.setText(context.getString(R.string.format_order_time_created,
                        order.getTimeCreated()));
            }

            if (showField(mask, SHOW_PHONE_NUMBER) && !order.getPhone().equals("")) {
                phone.setVisibility(View.VISIBLE);
                phone.setText(context.getString(R.string.format_order_phone, order.getPhone()));
            }

            if (showField(mask, SHOW_DESCRIPTION) && !order.getDescription().equals("")) {
                description.setVisibility(View.VISIBLE);
                description.setText(context.getString(R.string.format_order_description, order.getDescription()));
            }

            if (showField(mask, SHOW_PRICE) && !order.getPrice().equals("")) {
                price.setVisibility(View.VISIBLE);
                price.setText(context.getString(R.string.format_order_price, order.getPrice().split(",")[0]));
            }

            if (showField(mask, SHOW_OPTIONS) && !order.getOption().equals("0")) {
                options.setVisibility(View.VISIBLE);
                options.setText(context.getString(R.string.format_order_options, order.getOption()));
            }

            if (showField(mask, SHOW_ADDRESS_START) && !order.getFrom().equals("")) {
                from.setVisibility(View.VISIBLE);
                from.setText(context.getString(R.string.format_order_from, order.getFrom()));
            }

            if (showField(mask, SHOW_ADDRESS_END) && !order.getTo().equals("")) {
                to.setVisibility(View.VISIBLE);
                to.setText(context.getString(R.string.format_order_to, order.getTo()));
            }

            if (showField(mask, SHOW_DATE_START) && !order.getDate().equals("")) {
                date.setVisibility(View.VISIBLE);
                date.setText(context.getString(R.string.format_order_date, order.getDate()));
            }

            if (showField(mask, SHOW_TIME_START) && !order.getTime().equals("")) {
                time.setVisibility(View.VISIBLE);
                time.setText(context.getString(R.string.format_order_time, order.getTime()));
            }

            if (showField(mask, SHOW_ROUTE_LENGTH) && !order.getLength().equals("")) {
                distance.setVisibility(View.VISIBLE);
                distance.setText(context.getString(R.string.format_order_distance, order.getLength()));
            }

            if (showField(mask, SHOW_TARIFF) && !order.getTariffId().equals("")) {
                tariffId.setVisibility(View.VISIBLE);
                tariffId.setText(context.getString(R.string.format_order_tariff, order.getTariffId()));
            }
        }
    }
}
