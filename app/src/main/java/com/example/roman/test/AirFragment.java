package com.example.roman.test;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;

public class AirFragment extends Fragment {
    private Order[] ordersArray;

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

        ordersArray = new Order[]{
                new Order("вул. Головка, 8", "вул. Карла Лібкнехта, 10", 22),
                new Order("вул. Луценка, 22", "Південний Вокзал", 32),
                new Order("вул. Зіньківська, 5", "вул. Майдан Незалежності, 1А", 40),
                new Order("Стадіон Ворскла", "пров. Заячий, 1", 10),
                new Order("вул. Героїв АТО, 79", "вул. Станіславського, 8", 415),
                new Order("вул. Ціолковського, 41 ", "пров. Стешенка, 3", 27),
                new Order("вул. Івана Мазепи, 42", "вул. Європейська, 123", 314)
        };

        OrderAdapter mOrderAdapter = new OrderAdapter(getContext(), Arrays.asList(ordersArray));

        ListView listView = (ListView) rootView.findViewById(R.id.listview_air);
        listView.setAdapter(mOrderAdapter);
        return rootView;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };
}
