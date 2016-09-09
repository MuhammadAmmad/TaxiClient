package com.example.roman.test.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.ChatMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesListAdapter extends BaseAdapter {

    private final Context context;
    private final List<ChatMessage> messagesItems;

    static class ViewHolder {
        @BindView(R.id.label_date)
        TextView labelDate;

        @BindView(R.id.txtMsg)
        TextView textMessage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MessagesListAdapter(Context context, List<ChatMessage> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */

        ChatMessage message = messagesItems.get(position);
        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (message.getIsSelf()) {
            // message belongs to you, so load the right aligned layout
            view = mInflater.inflate(R.layout.list_item_msg_right, parent, false);
        } else {
            // message belongs to other person, load the left aligned layout
            view = mInflater.inflate(R.layout.list_item_msg_left, parent, false);
        }

        holder = new ViewHolder(view);

        // Populate the data into the template view using the data object
        holder.textMessage.setText(message.getMessage());
        holder.labelDate.setText(message.getDate());

        return view;
    }
}
