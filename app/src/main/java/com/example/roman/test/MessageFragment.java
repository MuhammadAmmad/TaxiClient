package com.example.roman.test;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.roman.test.adapters.MessageAdapter;
import com.example.roman.test.data.Message;
import com.example.roman.test.data.MessagesTable;

import java.util.List;


public class MessageFragment extends Fragment {

    static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        Cursor cursor = getActivity().getContentResolver().query(
                MessagesTable.CONTENT_URI, null, null, null, null);

        List<Message> messages = MessagesTable.getRows(cursor, false);
        MessageAdapter mMessageAdapter = new MessageAdapter(getContext(), messages);

        ListView mListView = (ListView) rootView.findViewById(R.id.list_view_messages);
        mListView.setAdapter(mMessageAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
        return rootView;
    }
}