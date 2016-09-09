package com.example.roman.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.roman.test.adapters.SectorAdapter;
import com.example.roman.test.data.Sector;
import com.example.roman.test.services.SocketService;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectorFragment extends Fragment {
    @BindView(R.id.list_view_sectors)
    ListView mListView;

    static SectorFragment newInstance() {
        return new SectorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sectors, container, false);
        ButterKnife.bind(this, view);
        List<Sector> sectors = Sector.listAll(Sector.class);

        SectorAdapter sectorAdapter = new SectorAdapter(getContext(), sectors);
        mListView.setAdapter(sectorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String sectorId = ((Sector) adapterView.getItemAtPosition(position)).getSectorId();
                try {
                    SocketService.getInstance().setSector(sectorId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().finish();
            }
        });

        return view;
    }
}
