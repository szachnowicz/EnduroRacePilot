package com.pwr.projekt.enduroracepilot.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sebastian on 2017-05-21.
 */

public class RouteDetalisViewAdpater extends ArrayAdapter<Route> {

    private final Context context;

    TextView textView;
    private RatingBar ratingBar;
    private ArrayList<Route> routeList;

    public RouteDetalisViewAdpater(@NonNull Context context, List<Route> markerOption) {
        super(context, -1, markerOption);
        this.context = context;
        routeList = (ArrayList<Route>) markerOption;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.routelistviewadapter, parent, false);
        textView = (TextView) rowView.findViewById(R.id.textViewRouteAdpater);
        ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBarRouteAdapter);
        ratingBar.setProgress(new Random().nextInt(5) + 2);
        ratingBar.setIsIndicator(true);
        textView.setText(routeList.get(position).toString());

        return rowView;

    }
}
