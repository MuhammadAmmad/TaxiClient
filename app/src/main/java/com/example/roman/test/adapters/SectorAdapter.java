package com.example.roman.test.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roman.test.R;
import com.example.roman.test.data.Sector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectorAdapter extends ArrayAdapter<Sector> {
    static class ViewHolder {
        @BindView(R.id.list_item_sector)
        TextView name;

        @BindView(R.id.list_item_drivers)
        TextView drivers;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public SectorAdapter(Context context, List<Sector> sectors) {
        super(context, R.layout.list_item_sector, sectors);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Sector sector = getItem(position);

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
            holder = (ViewHolder) view.getTag();
            holder.name.setText("");
            holder.drivers.setText("");
        }

        if (sector != null) {
            holder.name.setText(sector.getName());
            int drivers = sector.getDrivers();
            if (drivers != 0) {
                holder.drivers.setText(String.valueOf(drivers).toUpperCase());
            }
        }
        return view;
    }
}
