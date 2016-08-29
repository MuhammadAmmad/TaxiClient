package com.example.roman.test;

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
import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Functions;

import org.json.JSONException;

import java.util.List;

public class SectorFragment extends Fragment {
    private int positionChecked = ListView.INVALID_POSITION;

    static SectorFragment newInstance() {
        return new SectorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            SocketService.getInstance().getSectors("0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View rootView = inflater.inflate(R.layout.fragment_sectors, container, false);

        List<Sector> sectors = Functions.getSectorList(getContext());
        SectorAdapter sectorAdapter = new SectorAdapter(getContext(), sectors);

        final ListView mListView = (ListView) rootView.findViewById(R.id.list_view_sectors);
        mListView.setAdapter(sectorAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.sector_radio_button);
                radioButton.setChecked(!radioButton.isChecked());

                if (positionChecked != ListView.INVALID_POSITION && position != positionChecked) {
                    ((RadioButton) mListView.getChildAt(positionChecked)
                            .findViewById(R.id.sector_radio_button))
                            .setChecked(false);
                }

                String sectorId = ((Sector) adapterView.getItemAtPosition(position)).getId();
                try {
                    SocketService.getInstance().setSector(sectorId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                positionChecked = position;
                getActivity().finish();
            }
        });
        return rootView;
    }
}
