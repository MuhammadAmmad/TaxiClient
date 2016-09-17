package com.example.roman.test;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roman.test.adapters.MessagesListAdapter;
import com.example.roman.test.data.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MessageFragment extends Fragment {
    private List<Message> mMessages;
    private MessagesListAdapter mMessagesAdapter;

    @BindView(R.id.list_view_messages)
    ListView mListView;

    @BindView(R.id.no_messages)
    TextView noOrders;

    static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, view);

        try {
            mMessages = Message.listAll(Message.class);
            if (mMessages.size() == 0) {
                throw new SQLiteException();
            }
            noOrders.setVisibility(GONE);
            mListView.setVisibility(View.VISIBLE);
        } catch (SQLiteException e) {
            mMessages = new ArrayList<>();
            noOrders.setVisibility(View.VISIBLE);
            mListView.setVisibility(GONE);
        }

        mMessagesAdapter = new MessagesListAdapter(getActivity(), mMessages);
        mListView.setAdapter(mMessagesAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return view;
    }
}
