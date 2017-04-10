package com.pwr.projekt.enduroracepilot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.PoiAdapter;
import com.pwr.projekt.enduroracepilot.model.MapEntity.PoiItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoiPickerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private View view;

    public PoiPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_poi_picker, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewID);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PoiAdapter(PoiItem.createItemsToPicker());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
