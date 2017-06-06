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
import com.pwr.projekt.enduroracepilot.model.MapEntity.MapCalculator;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 2017-04-07.
 */

public class PointsDetalisAdapter extends ArrayAdapter<Point> {

    private Context context;
    private ArrayList<Point> pointsList;
    private TextView textView;
    private ImageView imageView;

    public PointsDetalisAdapter(@NonNull Context context, List<Point> markerOption) {
        super(context, -1, markerOption);
        this.context = context;
        pointsList = (ArrayList<Point>) markerOption;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.points_list_view_adapter, parent, false);
        textView = (TextView) rowView.findViewById(R.id.PRTextView);
        imageView = (ImageView) rowView.findViewById(R.id.RPImageView);
        Point point = pointsList.get(position);
        String text = "Numer punktu : "+point.getPointID()+"\n"
                + "odleglość od Startu : "+ MapCalculator.getDistaceFromPoint(pointsList,position)+"m";

        if (point.getPointID()==0)
        {
            text = "START";
        }
      if(point.getPointID()==pointsList.size()-1)
        {
            text = "Koniec";

        }

            textView.setText(text);
        imageView.setImageResource(R.drawable.start_flag);
        return rowView;
    }
}
