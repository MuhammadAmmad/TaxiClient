package com.example.roman.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roman.test.adapters.OrderAdapter;
import com.example.roman.test.data.Order;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.example.roman.test.DetailOrderFragment.DETAIL_ORDER;

public class AirFragment extends Fragment {
    @Inject Gson gson;
    private List<Order> mOrders;
    private OrderAdapter mOrderAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    static AirFragment newInstance() {
        return new AirFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mOrders = new ArrayList<>();
        mOrderAdapter = new OrderAdapter(getActivity(), mOrders, this);

        View view =  inflater.inflate(R.layout.fragment_air, container, false);

        ButterKnife.bind(this, view);
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new ItemDecorator(getActivity().getApplicationContext()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mOrderAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            mRecyclerView.smoothScrollToPosition(mPosition);
        }

        return view;
    }

    public void addOrders(Order[] orders) {
        Collections.addAll(mOrders, orders);
        mOrderAdapter.notifyDataSetChanged();
    }

    public void addOrder(Order order) {
        mOrders.add(order);
        mOrderAdapter.notifyItemInserted(mOrders.size() - 1);
    }

    public void removeOrder(String id) {
        for (int i = 0; i < mOrders.size(); i++) {
            if (mOrders.get(i).getOrderId().equals(id)) {
                removeAt(i);
                return;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);
    }

    public class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            mPosition = mRecyclerView.getChildLayoutPosition(view);
            Order order = mOrders.get(mPosition);
            Intent intent = new Intent(getActivity(), DetailOrderActivity.class);
            intent.putExtra(DETAIL_ORDER, gson.toJson(order));
            startActivity(intent);
        }
    }

    private void removeAt(int position) {
        mOrders.remove(position);
        mOrderAdapter.notifyItemRemoved(position);
        mOrderAdapter.notifyItemRangeChanged(position, mOrders.size());
    }
}
