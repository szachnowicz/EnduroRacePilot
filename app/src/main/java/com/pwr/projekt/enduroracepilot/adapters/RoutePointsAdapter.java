package com.pwr.projekt.enduroracepilot.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 2017-04-07.
 */

public class RoutePointsAdapter extends ArrayAdapter<Point> {

    private Context context;
    private ArrayList<Point> markerOptions;
    private TextView textView;
    private ImageView imageView;

    public RoutePointsAdapter(@NonNull Context context, List<Point> markerOption) {
        super(context, -1, markerOption);
        this.context = context;
        markerOptions = (ArrayList<Point>) markerOption;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.points_list_view_adapter, parent, false);
        textView = (TextView) rowView.findViewById(R.id.PRTextView);
        imageView = (ImageView) rowView.findViewById(R.id.RPImageView);
        textView.setText(markerOptions.get(position).toString());
        imageView.setImageResource(R.drawable.start_flag);
        return rowView;
    }
}
