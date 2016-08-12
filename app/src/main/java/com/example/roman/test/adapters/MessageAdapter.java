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

public class MessageAdapter extends ArrayAdapter<Message> {

    private static class ViewHolder {
        TextView message;
        TextView date;
    }

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, R.layout.list_item_sector, messages);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Message message = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for now
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_sector, parent, false);

            viewHolder.message = (TextView) convertView.findViewById(R.id.list_item_message);
            viewHolder.date = (TextView) convertView.findViewById(R.id.list_item_date);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        if (message != null) {
            viewHolder.date.setText(message.date);
            viewHolder.message.setText(message.message);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
