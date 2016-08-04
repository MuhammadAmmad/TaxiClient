package com.example.roman.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    static MyFragment newInstance(String message) {
        MyFragment f = new MyFragment();
        Bundle bd1 = new Bundle(1);
        bd1.putString(EXTRA_MESSAGE, message);
        f.setArguments(bd1);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String message = getArguments().getString(EXTRA_MESSAGE);

        View v = inflater.inflate(R.layout.my_fragment_layout,
                container, false);
        TextView messageTextView = (TextView) v.findViewById(R.id.textView);
        messageTextView.setText(message);

        return v;
    }
}
