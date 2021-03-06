package com.example.roman.test.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    static class ViewHolder {

        @BindView(R.id.list_item_time)
        TextView date;

        @BindView(R.id.list_item_message)
        TextView message;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MessagesListAdapter(Context context, List<Message> messages) {
        super(context, R.layout.list_item_message, messages);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Message message = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder;

        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for now
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_message, parent, false);

            holder = new ViewHolder(view);
            // Cache the viewHolder object inside the fresh view
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.message.setText("");
            holder.date.setText("");
        }

        if (message != null) {
            holder.message.setText(message.getMessage());
            holder.date.setText(message.getDate());
        }

        return view;
    }
}

