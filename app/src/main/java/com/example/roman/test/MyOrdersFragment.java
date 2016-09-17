package com.example.roman.test;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roman.test.adapters.MyOrdersAdapter;
import com.example.roman.test.data.Record;
import com.example.roman.test.utilities.Functions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MyOrdersFragment extends Fragment {
    private List<Record> mOrders;
    private MyOrdersAdapter mMyOrdersAdapter;

    @BindView(R.id.list_view_my_orders)
    ListView mListView;

    @BindView(R.id.no_orders)
    TextView noOrders;

    static MyOrdersFragment newInstance() {
        return new MyOrdersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ButterKnife.bind(this, view);

        try {
            mOrders = Record.listAll(Record.class);
            noOrders.setVisibility(GONE);
            mListView.setVisibility(View.VISIBLE);
        } catch (SQLiteException e) {
            mOrders = new ArrayList<>();
            noOrders.setVisibility(View.VISIBLE);
            mListView.setVisibility(GONE);
        }

        mMyOrdersAdapter = new MyOrdersAdapter(getActivity(), mOrders);
        mListView.setAdapter(mMyOrdersAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Record record = (Record) adapterView.getItemAtPosition(i);
                Functions.getDialog(getActivity(), record.getDate(), record.getFromAddress())
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        return view;
    }
}
