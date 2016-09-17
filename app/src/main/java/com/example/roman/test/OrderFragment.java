package com.example.roman.test;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.roman.test.data.Record;
import com.example.roman.test.services.SocketService;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.example.roman.test.utilities.Constants.ORDER_STATUS_WAITING;
import static com.example.roman.test.utilities.Constants.SHOW_ADDRESS_END;
import static com.example.roman.test.utilities.Constants.SHOW_ADDRESS_START;
import static com.example.roman.test.utilities.Constants.SHOW_DATE_START;
import static com.example.roman.test.utilities.Constants.SHOW_DESCRIPTION;
import static com.example.roman.test.utilities.Constants.SHOW_PHONE_NUMBER;
import static com.example.roman.test.utilities.Constants.SHOW_PRICE;
import static com.example.roman.test.utilities.Constants.SHOW_ROUTE_LENGTH;
import static com.example.roman.test.utilities.Constants.SHOW_TARIFF;
import static com.example.roman.test.utilities.Constants.SHOW_TIME_CREATED;
import static com.example.roman.test.utilities.Constants.SHOW_TIME_START;
import static com.example.roman.test.utilities.Functions.showField;

public class OrderFragment extends Fragment {
    @BindView(R.id.no_orders)
    TextView noOrders;

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

    @BindView(R.id.action_actions)
    Button actions;

    @Inject SharedPreferences prefs;
    @Inject Gson gson;

    static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);

        Record record = null;
        try {
            record = Record.find(Record.class, null, null, null, "id DESC", "1").get(0);
            setFragment(record);
            noOrders.setVisibility(GONE);
        } catch (SQLiteException | IndexOutOfBoundsException e) {
            noOrders.setVisibility(View.VISIBLE);
        }

        map.setClickable(false);

        final Record finalRecord = record;
        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalRecord != null) {
                    setStatus(finalRecord.getRecordId()).show();
                }
            }
        });

        return view;
    }

    private void setFragment(Record order) {
        if (order != null) {
//            int mask = Integer.parseInt(Functions
//                    .getFromPreferences(NEW_ORDER_MASK, prefs));
            int mask = 1216;

            if (showField(mask, SHOW_TIME_CREATED) && !order.getTimeCreated().equals("")) {
                timeCreated.setVisibility(View.VISIBLE);
                timeCreated.setText(getString(R.string.format_order_time_created,
                        order.getTimeCreated()));
            }

            if (showField(mask, SHOW_PHONE_NUMBER) && !order.getPhone().equals("")) {
                phone.setVisibility(View.VISIBLE);
                phone.setText(getString(R.string.format_order_phone, order.getPhone()));
            }

            if (showField(mask, SHOW_DESCRIPTION) && !order.getDescription().equals("")) {
                description.setVisibility(View.VISIBLE);
                description.setText(getString(R.string.format_order_description, order.getDescription()));
            }

            if (showField(mask, SHOW_PRICE) && !order.getPrice().equals("")) {
                price.setVisibility(View.VISIBLE);
                price.setText(getString(R.string.format_order_price, order.getPrice().split(",")[0]));
            }

//            if (showField(mask, SHOW_OPTIONS) && !order.getOption().equals("0")) {
//                options.setVisibility(View.VISIBLE);
//                options.setText(getString(R.string.format_order_options, order.getOption()));
//            }

            if (showField(mask, SHOW_ADDRESS_START) && !order.getFromAddress().equals("")) {
                from.setVisibility(View.VISIBLE);
                from.setText(getString(R.string.format_order_from, order.getFromAddress()));
            }

            if (showField(mask, SHOW_ADDRESS_END) && !order.getToAddress().equals("")) {
                to.setVisibility(View.VISIBLE);
                to.setText(getString(R.string.format_order_to, order.getToAddress()));
            }

            if (showField(mask, SHOW_DATE_START) && !order.getDate().equals("")) {
                date.setVisibility(View.VISIBLE);
                date.setText(getString(R.string.format_order_date, order.getDate()));
            }

            if (showField(mask, SHOW_TIME_START) && !order.getTime().equals("")) {
                time.setVisibility(View.VISIBLE);
                time.setText(getString(R.string.format_order_time, order.getTime()));
            }

            if (showField(mask, SHOW_ROUTE_LENGTH) && !order.getLength().equals("")) {
                distance.setVisibility(View.VISIBLE);
                distance.setText(getString(R.string.format_order_distance, order.getLength()));
            }

            if (showField(mask, SHOW_TARIFF) && !order.getTariffId().equals("")) {
                tariffId.setVisibility(View.VISIBLE);
                tariffId.setText(getString(R.string.format_order_tariff, order.getTariffId()));
            }
        }
    }

    public void newOrder() {
        Record record = Record.find(Record.class, null, null, null, "id DESC", "1").get(0);
        setFragment(record);
        noOrders.setVisibility(GONE);
    }

    private AlertDialog setStatus(final String orderId) {
        List<String> myOptions = Arrays.asList((getActivity().getResources()
                .getStringArray(R.array.actions_values)));

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.main_order_actions)
                .setSingleChoiceItems(R.array.actions_titles, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 2) {
                                    try {
                                        SocketService.getInstance().closeOrder(orderId);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (i == 0) {
                                    try {
                                        SocketService.getInstance().setOrderStatus(orderId, ORDER_STATUS_WAITING);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                dialog.dismiss();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
