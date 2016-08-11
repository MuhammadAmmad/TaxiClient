package com.example.roman.test;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;

import java.util.List;

public class SectorFragment extends Fragment {
    private List<Sector> sectorArray;
    private SectorAdapter mSectorAdapter;

    static SectorFragment newInstance() {
        SectorFragment f = new SectorFragment();
        Bundle bd1 = new Bundle(1);
        f.setArguments(bd1);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sectors, container, false);

        Cursor cursor = getActivity().getContentResolver().query(
                SectorsTable.CONTENT_URI, null, null, null, null);

        sectorArray = SectorsTable.getRows(cursor, false);
        mSectorAdapter = new SectorAdapter(getContext(), sectorArray);

        ListView mListView = (ListView) rootView.findViewById(R.id.list_view_sectors);
        mListView.setAdapter(mSectorAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.sector_check_box);
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        return rootView;
    }

    public void addSectors(List<Sector> sectors) {
        for (Sector s : sectors) {
            mSectorAdapter.add(s);
        }
    }
}
