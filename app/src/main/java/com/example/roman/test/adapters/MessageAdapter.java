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

public class MessageAdapter extends ArrayAdapter<Message> {

    static class ViewHolder {
        @BindView(R.id.list_item_message)
        TextView message;

        @BindView(R.id.list_item_date)
        TextView date;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, R.layout.list_item_sector, messages);
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
            view = inflater.inflate(R.layout.list_item_sector, parent, false);

            holder = new ViewHolder(view);

            // Cache the viewHolder object inside the fresh view
            view.setTag(holder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            holder = (ViewHolder) view.getTag();
        }

        // Populate the data into the template view using the data object
        if (message != null) {
            holder.date.setText(message.getDate());
            holder.message.setText(message.getMessage());
        }
        return view;
    }
}
