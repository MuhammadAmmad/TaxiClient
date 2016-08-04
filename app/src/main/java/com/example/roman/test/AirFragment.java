package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.roman.test.socket.SocketService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AirFragment extends Fragment implements SocketService.Callbacks {
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    public static final String mBroadcastIntegerAction = "com.truiton.broadcast.integer";
    public static final String mBroadcastArrayListAction = "com.truiton.broadcast.arraylist";

    private ArrayAdapter<String> mAirAdapter;
    String [] ordersArray;

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

        ordersArray = new String[]{
                "вул. Головка, 8 - вул. Карла Лібкнехта, 10",
                "вул. Луценка, 22 - Південний Вокзал",
                "вул. Зіньківська, 5 - вул. Майдан нежалежності 1А",
                "Стадіон Ворскла - вул. Станіславського, 8",
                "вул. Героїв АТО, 79 - пров. Стешенка, 3",
                "вул. Ціолковського, 41 - пров. Заячий, 1",
                "вул. Івана Мазепи, 42 - вул. Європейська, 123",
        };
        List<String> orderForecast = new ArrayList<>(
                Arrays.asList(ordersArray));

        mAirAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_air,
                R.id.list_item_air_textview,
                orderForecast);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_air);
        listView.setAdapter(mAirAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderInfo = mAirAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), OrderActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, orderInfo);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            mTextView.setText(mTextView.getText()
//                    + "Broadcast From Service: \n");
//            if (intent.getAction().equals(mBroadcastStringAction)) {
//                mTextView.setText(mTextView.getText()
//                        + intent.getStringExtra("Data") + "\n\n");
//            } else if (intent.getAction().equals(mBroadcastIntegerAction)) {
//                mTextView.setText(mTextView.getText().toString()
//                        + intent.getIntExtra("Data", 0) + "\n\n");
//            } else if (intent.getAction().equals(mBroadcastArrayListAction)) {
//                mTextView.setText(mTextView.getText()
//                        + intent.getStringArrayListExtra("Data").toString()
//                        + "\n\n");
//                Intent stopIntent = new Intent(MainActivity.this,
//                        BroadcastService.class);
//                stopService(stopIntent);
//            }
        }
    };

    @Override
    public void updateAir(String message) {
        return;
    }
}
