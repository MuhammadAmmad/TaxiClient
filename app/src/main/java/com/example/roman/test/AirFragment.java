package com.example.roman.test;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.roman.test.adapters.AirAdapter;
import com.example.roman.test.data.AirRecord;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.DetailOrderFragment.DETAIL_ORDER;
import static com.example.roman.test.utilities.Constants.ORDER_ID;

public class AirFragment extends Fragment {
    @BindView(R.id.air_progress)
    ProgressBar progressBar;

    @BindView(R.id.list_view_orders)
    ListView mListView;

    @Inject Gson gson;

    private static final String SELECTED_KEY = "selected_position";
    private static final String OFFSET = "offset";

    private boolean first;
    private int mPosition = ListView.INVALID_POSITION;
    private List<AirRecord> mOrders;
    private AirAdapter mAirAdapter;

    static AirFragment newInstance(boolean firstTime) {
        AirFragment airFragment = new AirFragment();

        Bundle arguments = new Bundle();
        arguments.putBoolean("FIRST", firstTime);
        airFragment.setArguments(arguments);

        return airFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first = getArguments().getBoolean("FIRST", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_air, container, false);
        ButterKnife.bind(this, view);
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);

        mOrders = new ArrayList<>();
        mAirAdapter = new AirAdapter(getActivity(), mOrders);

        mListView.setAdapter(mAirAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AirRecord order = mOrders.get(i);
                Intent intent = new Intent(getActivity(), DetailOrderActivity.class);
                intent.putExtra(DETAIL_ORDER, order.getFromAddress());
                intent.putExtra(ORDER_ID, order.getRecordId());
                startActivity(intent);
            }
        });

        try {
            addOrders(AirRecord.listAll(AirRecord.class));
        } catch (SQLiteException e) {
            Log.e("CHECK", "first time");
        }


        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(SELECTED_KEY);
            int offset = savedInstanceState.getInt(OFFSET);

            if (index != ListView.INVALID_POSITION) {
                mListView.setSelectionFromTop(index, offset);
            }
        }

        return view;
    }

    public void addOrders(List<AirRecord> orders) {
        mOrders.addAll(orders);
        mAirAdapter.notifyDataSetChanged();

        mListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void addOrder(AirRecord order) {
        if (mOrders !=  null) {
            mOrders.add(order);
            mAirAdapter.notifyDataSetChanged();
        }
    }

    public void removeOrder(String id) {
        if (mOrders != null) {
            for (int i = 0; i < mOrders.size(); i++) {
                if (mOrders.get(i).getRecordId().equals(id)) {
                    mAirAdapter.remove(mOrders.get(i));
                    return;
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            int index = mListView.getFirstVisiblePosition();
            View v = mListView.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());

            outState.putInt(SELECTED_KEY, index);
            outState.putInt(OFFSET, top);
        }

        super.onSaveInstanceState(outState);
    }
}
