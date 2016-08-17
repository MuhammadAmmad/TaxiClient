package com.example.roman.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.roman.test.data.Order;
import com.example.roman.test.services.SocketService;
import com.google.gson.Gson;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.utilities.Constants.ORDER_STATUS_NEW;

public class DetailOrderFragment extends Fragment {
    static final String DETAIL_ORDER = "ORDER";

    @BindView(R.id.order_from)
    TextView from;

    @BindView(R.id.order_to)
    TextView to;

    @BindView(R.id.order_price)
    TextView price;

    @BindView(R.id.order_phone)
    TextView phone;

    @BindView(R.id.order_description)
    TextView description;

    @BindView(R.id.order_option_list)
    TextView options;

    @BindView(R.id.order_sector)
    TextView sector;

    @BindView(R.id.order_distance)
    TextView distance;

    @BindView(R.id.action_take)
    Button take;

    static DetailOrderFragment newInstance() {
        DetailOrderFragment f = new DetailOrderFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getContext();
        View view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            final Order order = new Gson().fromJson(args.getString(DETAIL_ORDER), Order.class);
            if (order != null) {
                from.setText(context.getString(R.string.format_order_from, order.getFrom()));
                to.setText(context.getString(R.string.format_order_to, order.getTo()));
                price.setText(context.getString(R.string.format_order_price, order.getPrice().split(",")[0]));
                phone.setText(context.getString(R.string.format_order_phone, order.getPhone()));
                description.setText(context.getString(R.string.format_order_description, order.getDescription()));
                options.setText(context.getString(R.string.format_order_options, order.getOption()));
                sector.setText(context.getString(R.string.format_order_sector, order.getSectorFrom()));
                distance.setText(context.getString(R.string.format_order_distance, order.getLength()));

                take.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            SocketService.getInstance().setStatus(ORDER_STATUS_NEW, order.getOrderId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        return view;
    }
}
