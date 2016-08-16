package com.example.roman.test;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.roman.test.adapters.SectorAdapter;
import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;
import com.example.roman.test.services.SocketService;

import java.util.List;

public class SectorFragment extends Fragment {
    private int positionChecked = ListView.INVALID_POSITION;

    static SectorFragment newInstance() {
        SectorFragment f = new SectorFragment();
        Bundle bd1 = new Bundle(1);
        f.setArguments(bd1);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sectors, container, false);
        Cursor cursor = getActivity().getContentResolver().query(
                SectorsTable.CONTENT_URI, null, null, null, null);

        List<Sector> mSectors = SectorsTable.getRows(cursor, false);
        SectorAdapter mSectorAdapter = new SectorAdapter(getContext(), mSectors);

        final ListView mListView = (ListView) rootView.findViewById(R.id.list_view_sectors);
        mListView.setAdapter(mSectorAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.sector_radio_button);
                radioButton.setChecked(!radioButton.isChecked());

                if (positionChecked != ListView.INVALID_POSITION) {
                    ((RadioButton) mListView.getChildAt(positionChecked)
                            .findViewById(R.id.sector_radio_button)).setChecked(false);
                }

                String sectorId = ((Sector) adapterView.getItemAtPosition(position)).getId();
                SocketService.getInstance().setSector(sectorId);
                positionChecked = position;
            }
        });

        return rootView;
    }
}
