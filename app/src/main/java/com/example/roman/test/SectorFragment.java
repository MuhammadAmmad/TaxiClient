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
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;
    static final String SELECTED_KEY = "selected_order";

    static SectorFragment newInstance(String sectorId) {
        SectorFragment fragment = new SectorFragment();

        Bundle args = new Bundle();
        args.putString(SELECTED_KEY, sectorId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            SocketService.getInstance().getSectors("0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String id = getArguments().getString(SELECTED_KEY);
        final View view = inflater.inflate(R.layout.fragment_sectors, container, false);

        List<Sector> sectors = Functions.getSectorList(getContext());
        int position = -1;
        for (int i = 0; i < sectors.size(); i++) {
            if (sectors.get(i).getId().equals(id)) {
                position = i;
            }
        }

        SectorAdapter sectorAdapter = new SectorAdapter(getContext(), sectors);
        mListView = (ListView) view.findViewById(R.id.list_view_sectors);
        mListView.setAdapter(sectorAdapter);

        final int finalPosition = position;
        mListView.post(new Runnable() {
            @Override
            public void run() {
                if (id != null && !id.equals("")) {
                    ((RadioButton) getViewByPosition(finalPosition, mListView)
                            .findViewById(R.id.button_text)
                            .findViewById(R.id.sector_radio_button))
                            .setChecked(true);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.sector_radio_button);
                radioButton.setChecked(!radioButton.isChecked());
                if (mPosition != ListView.INVALID_POSITION) {
                    ((RadioButton) mListView.getChildAt(mPosition)
                            .findViewById(R.id.sector_radio_button))
                            .setChecked(false);
                }

                String sectorId = ((Sector) adapterView.getItemAtPosition(position)).getId();
                try {
                    SocketService.getInstance().setSector(sectorId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mPosition = position;
                getActivity().finish();
            }
        });

        return view;
    }


    private static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
