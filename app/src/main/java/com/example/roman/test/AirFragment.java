package com.example.roman.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.test.adapters.OrderAdapter;
import com.example.roman.test.data.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirFragment extends Fragment {
    private List<Order> orders;
    private OrderAdapter mOrderAdapter;

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

        orders = new ArrayList<>(Arrays.asList(new Order[]{
                new Order("вул. Головка, 8", "вул. Карла Лібкнехта, 10", 22, "1"),
                new Order("вул. Луценка, 22", "Південний Вокзал", 32, "2"),
                new Order("вул. Зіньківська, 5", "вул. Сакко, 58", 40, "3"),
                new Order("Стадіон Ворскла", "пров. Заячий, 1", 10, "4"),
                new Order("вул. Героїв АТО, 79", "вул. Станіславського, 8", 415, "5"),
                new Order("вул. Ціолковського, 41 ", "пров. Стешенка, 3", 27, "6"),
                new Order("вул. Івана Мазепи, 42", "вул. Європейська, 123", 314, "7"),
        }));

        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(new ItemDecorator(
                getActivity().getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OrderAdapter(getActivity(), orders));

        return v;

//        View rootView = inflater.inflate(R.layout.fragment_air, container, false);
//
//        MainActivity activity = (MainActivity) getActivity();
//
//        View v = inflater.inflate(R.layout.fragment_list_view, container, false);
//        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new OrderAdapter(ordersArray));
//
////        mOrderAdapter = new OrderAdapter(getContext(), ordersArray);
////
////        ListView listView = (ListView) rootView.findViewById(R.id.list_view_air);
////        listView.setAdapter(mOrderAdapter);
//        return rootView;
    }

//    public void addOrders(Order[] orders) {
//        for (Order order : orders) {
//            addOrder(order);
//        }
//    }

//    public void addOrder(Order order) {
//        mOrderAdapter.add(order);
//    }
//
//    public void removeOrder(String id) {
//        for (Order order : ordersArray) {
//            if (order.getOrderId().equals(id)) {
//                mOrderAdapter.remove(order);
//                return;
//            }
//        }
//    }
}
