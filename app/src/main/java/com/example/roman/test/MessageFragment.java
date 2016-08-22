package com.example.roman.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roman.test.adapters.MessageAdapter;
import com.example.roman.test.data.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {
    private int positionChecked = ListView.INVALID_POSITION;

    static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

//        List<Message> messages = Functions.getMessageList(getContext());
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("1", "What the hell", "20/08/2016"));
        messages.add(new Message("2", "What the fuck", "20/08/2016"));
        messages.add(new Message("1", "I have no idea", "20/08/2016"));
        MessageAdapter messageAdapter = new MessageAdapter(getContext(), messages);

        final ListView mListView = (ListView) rootView.findViewById(R.id.list_view_messages);
        mListView.setAdapter(messageAdapter);

        return rootView;
    }
}