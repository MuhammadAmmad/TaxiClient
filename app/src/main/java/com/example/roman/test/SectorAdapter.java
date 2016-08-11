package com.example.roman.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roman.test.data.Sector;

import java.util.List;

public class SectorAdapter extends ArrayAdapter<Sector> {

    private static class ViewHolder {
        TextView sectorView;
        TextView driversView;
    }

    public SectorAdapter(Context context, List<Sector> sectors) {
        super(context, R.layout.list_item_sector, sectors);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Sector sector = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for now
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_air, parent, false);
            viewHolder.driversView = (TextView) convertView.findViewById(R.id.list_item_drivers);
            viewHolder.sectorView = (TextView) convertView.findViewById(R.id.list_item_sector);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.sectorView.setText(sector.name);
        viewHolder.driversView.setText(sector.numOfDrivers);

        // Return the completed view to render on screen
        return convertView;
    }
}
