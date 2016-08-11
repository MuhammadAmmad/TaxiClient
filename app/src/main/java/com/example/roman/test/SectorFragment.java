package com.example.roman.test;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;

import java.util.List;

public class SectorFragment extends Fragment {
    private List<Sector> sectorArray;
    private SectorAdapter mSectorAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_air, container, false);

        Cursor cursor = getActivity().getContentResolver().query(
                SectorsTable.CONTENT_URI, null, null, null, null);

        sectorArray = SectorsTable.getRows(cursor, false);

        mSectorAdapter = new SectorAdapter(getContext(), sectorArray);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_air);
        listView.setAdapter(mSectorAdapter);

        return rootView;
    }

    public void addOrders(List<Sector> sectors) {
        for (Sector s : sectors) {
            mSectorAdapter.add(s);
        }
    }
}
