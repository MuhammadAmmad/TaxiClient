package com.example.roman.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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

import static com.example.roman.test.utilities.Constants.ERROR;
import static com.example.roman.test.utilities.Constants.ERROR_NONE;
import static com.example.roman.test.utilities.Constants.MAIN_INTENT;
import static com.example.roman.test.utilities.Constants.METHOD;
import static com.example.roman.test.utilities.Constants.METHOD_DELETE_ORDER;
import static com.example.roman.test.utilities.Constants.ORDER_ID;
import static com.example.roman.test.utilities.Constants.RESPONSE;

public class DetailOrderFragment extends Fragment {
    static final String DETAIL_ORDER = "ORDER";

    @BindView(R.id.order_info)
    TextView orderInfo;

    @BindView(R.id.detail_action_take)
    Button take;

    @BindView(R.id.detail_action_waiting_time)
    Button timeWait;

    @BindView(R.id.detail_action_cancel)
    Button cancel;

    private String mWaitingTime;

    @Inject SharedPreferences prefs;
    @Inject Gson gson;

    static DetailOrderFragment newInstance() {
        return new DetailOrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        ButterKnife.bind(this, view);
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);

        Bundle args = getArguments();

        if (args != null) {
            final String fromAddress = args.getString(DETAIL_ORDER);
            final String recordId = args.getString(ORDER_ID);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mWaitingTime = preferences.getString(getString(R.string.pref_waiting_time_key), getString(R.string.pref_waiting_time_default));

            if (mWaitingTime.equals("-1")) {
                mWaitingTime = getString(R.string.pref_waiting_time_default);
            }

            if (fromAddress != null) {
                orderInfo.setText(fromAddress);

                final String finalWaitingTime = mWaitingTime;

                take.setText(getString(R.string.format_take_order, mWaitingTime));
                take.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            SocketService.getInstance().takeOrder(recordId, finalWaitingTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getActivity().finish();
                    }
                });

                timeWait.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimeDialog(recordId).show();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                });
            }
        }
        return view;
    }

    private AlertDialog TimeDialog(final String recordId) {
        List<String> myOptions = Arrays.asList((getResources()
                .getStringArray(R.array.pref_waiting_time_values)));
        int index = myOptions.indexOf(mWaitingTime);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.detail_waiting_time)
                .setSingleChoiceItems(R.array.pref_waiting_time_titles, index,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        final String waitingTime = ((String) lw.getAdapter()
                                .getItem(lw.getCheckedItemPosition())).replaceAll("[^0-9]", "");

                        try {
                            SocketService.getInstance().takeOrder(recordId, waitingTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        removeOrder(recordId);
                        dialog.dismiss();
                        getActivity().finish();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void removeOrder(String orderId) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(ERROR, ERROR_NONE);
        broadcastIntent.putExtra(METHOD, METHOD_DELETE_ORDER);
        broadcastIntent.putExtra(RESPONSE, orderId);
        broadcastIntent.setAction(MAIN_INTENT);
        SocketService.getInstance().sendBroadcast(broadcastIntent);
    }
}
