package com.example.roman.test;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AirFragment extends Fragment {
    private List<Order> mOrders;
    private OrderAdapter mOrderAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = ListView.INVALID_POSITION;

    static AirFragment newInstance() {
        AirFragment f = new AirFragment();
        Bundle bd1 = new Bundle(1);
        f.setArguments(bd1);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mOrders = new ArrayList<>();
        mOrderAdapter = new OrderAdapter(getActivity(), mOrders, this);

        View view =  inflater.inflate(R.layout.fragment_list_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new ItemDecorator(getActivity().getApplicationContext()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mOrderAdapter);

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
        for (Iterator<Order> iterator = mOrders.iterator(); iterator.hasNext();) {
            Order order = iterator.next();
            if (order.getOrderId().equals(id)) {
                int position = mOrders.indexOf(order);
                iterator.remove();
                mOrderAdapter.notifyItemRemoved(position);
                // TODO check later if workd properly
                mOrderAdapter.notifyItemRangeChanged(position, mOrders.size());
                return;
            }
        }
    }

    public interface Callback {
        void onItemSelected(Order order);
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            mPosition = mRecyclerView.getChildLayoutPosition(view);
            Order order = mOrders.get(mPosition);
            ((Callback) getActivity()).onItemSelected(order);
        }
    }
}
