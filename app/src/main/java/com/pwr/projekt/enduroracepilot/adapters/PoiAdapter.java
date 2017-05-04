package com.pwr.projekt.enduroracepilot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.AddingPOIFragmentCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Poi;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sebastian on 2017-04-05.
 */

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Poi> poiItems;
    private AddingPOIFragmentCallback onSelectedPOIListener;

    public PoiAdapter(Poi[] poi, AddingPOIFragmentCallback _onSelectedPOIListener, Context _context ) {
        poiItems = new ArrayList();
        this.poiItems.addAll(Arrays.asList(poi));
        onSelectedPOIListener = _onSelectedPOIListener;
        context = _context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_adapter_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(poiItems.get(position).getDiscription());
        holder.poiImg.setImageResource(poiItems.get(position).getDrawable());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poiItems.get(position).playSound(context);
                onSelectedPOIListener.onPoiItemChoose(poiItems.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return poiItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView poiImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.poiAdapterTextView);
            poiImg = (ImageView) itemView.findViewById(R.id.poiAdapterImageView);

        }
    }
}
