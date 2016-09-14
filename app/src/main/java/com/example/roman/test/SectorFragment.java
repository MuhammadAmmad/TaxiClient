package com.example.roman.test;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import static android.R.attr.duration;

public class SectorFragment extends Fragment {
    private int mPosition;

    @BindView(R.id.list_view_sectors)
    ListView mListView;

    static SectorFragment newInstance(String sectorId) {
        SectorFragment sectorFragment = new SectorFragment();

        Bundle args = new Bundle();
        args.putInt("sectorId", Integer.parseInt(sectorId));
        sectorFragment.setArguments(args);

        return sectorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("sectorId", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sectors, container, false);
        ButterKnife.bind(this, view);
        List<Sector> sectors = Sector.listAll(Sector.class);

        SectorAdapter sectorAdapter = new SectorAdapter(getContext(), sectors);
        mListView.setAdapter(sectorAdapter);
        if (mPosition != -1) {
            final int position = getIndexById(sectors, String.valueOf(mPosition));
            if (position != -1 ) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPosition(position);
                    }
                });
            }
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String sectorId = "0";
                Sector sector = ((Sector) adapterView.getItemAtPosition(position));

                if (!sector.isChecked()) {
                    sectorId = sector.getSectorId();
                }

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

    private int getIndexById(List<Sector> sectorList, String sectorId) {
        for (int i = 0; i < sectorList.size(); i++) {
            if (sectorList.get(i).getSectorId().equals(sectorId)) {
                return i;
            }
        }

        return -1;
    }
}
