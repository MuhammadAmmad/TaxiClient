package com.example.roman.test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roman.test.adapters.SectorAdapter;
import com.example.roman.test.data.Sector;
import com.example.roman.test.utilities.Functions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public HomeFragment() { }

    public static HomeFragment newInstance(String param1, String param2) {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<Sector> sectors = Functions.getSectorList(getContext());
        SectorAdapter sectorAdapter = new SectorAdapter(getContext(), sectors);

        final ListView mListView = (ListView) rootView.findViewById(R.id.list_view_sectors);
        mListView.setAdapter(sectorAdapter);

        return rootView;
    }
}
