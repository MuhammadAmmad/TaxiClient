package com.example.roman.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirFragment extends Fragment {
    private List<Order> ordersArray;
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
        View rootView = inflater.inflate(R.layout.fragment_air, container, false);

        ordersArray = new ArrayList<>(Arrays.asList(new Order[]{
                new Order("вул. Головка, 8", "вул. Карла Лібкнехта, 10", 22, 1),
                new Order("вул. Луценка, 22", "Південний Вокзал", 32, 2),
                new Order("вул. Зіньківська, 5", "вул. Майдан Незалежності, 1А", 40, 3),
                new Order("Стадіон Ворскла", "пров. Заячий, 1", 10, 4),
                new Order("вул. Героїв АТО, 79", "вул. Станіславського, 8", 415, 5),
                new Order("вул. Ціолковського, 41 ", "пров. Стешенка, 3", 27, 6),
                new Order("вул. Івана Мазепи, 42", "вул. Європейська, 123", 314, 7)
        }));

        mOrderAdapter = new OrderAdapter(getContext(), ordersArray);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_air);
        listView.setAdapter(mOrderAdapter);

        return rootView;
    }

    public void addOrder() {
        mOrderAdapter.add(new Order("вул. Головка, 8", "вул. Карла Лібкнехта, 10", 22, 1));
    }

    public void removeOrder(int id) {
        for (Order order : ordersArray) {
            if (order.id == id) {
                mOrderAdapter.remove(order);
                return;
            }
        }
    }
}
