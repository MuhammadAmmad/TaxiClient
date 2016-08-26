package com.example.roman.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roman.test.adapters.MessagesListAdapter;
import com.example.roman.test.data.ChatMessage;
import com.example.roman.test.utilities.Functions;

import java.util.List;


public class MessageFragment extends Fragment {
    private int positionChecked = ListView.INVALID_POSITION;

    static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_dialog, container, false);

        List<ChatMessage> messages = Functions.getMessageList(getActivity());
        MessagesListAdapter messageAdapter = new MessagesListAdapter(getContext(), messages);

        final ListView mListView = (ListView) rootView.findViewById(R.id.list_view_messages);
        mListView.setAdapter(messageAdapter);

        return rootView;
    }
}