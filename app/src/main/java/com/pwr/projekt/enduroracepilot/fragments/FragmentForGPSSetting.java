package com.pwr.projekt.enduroracepilot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForGPSSetting extends Fragment {

    @BindView(R.id.seekBarDistanceToPath)
    SeekBar distaneToPathSeekBar;
    @BindView(R.id.seekBarDistanceToPoi)
    SeekBar distaneToPoiSeekBar;
    @BindView(R.id.seekBarGpsRefresh)
    SeekBar gpsRefreshSeekBar;
    @BindView(R.id.textViewDistanceToPoi)
    TextView distanceToPoiTextView;

    @BindView(R.id.textViewDistanceToPath)
    TextView distanceToPathTextView;
    @BindView(R.id.textViewGpsRefresh)
    TextView gpsRefresTextView;
    @BindView(R.id.radioButtonPoi)
    RadioButton poiRadioButton;
    @BindView(R.id.radioButtonDisRoute)
    RadioButton distaneToRouteRadioButton;

    private View view;
    private String distaneToPathText = "Maxymalna odlegość od sciezki : ";
    private String distaneToPoiText = "Maxymalna odległość do POI : ";
    private String gpsRefreshText = "Częstotliwość odswieżania gps : ";
    private SharedPref sharedPref;
    private boolean showPoiCircle = true;
    private boolean showPathCircle = true;

    public FragmentForGPSSetting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_for_gpssetting, container, false);
        sharedPref = new SharedPref(getContext());
        ButterKnife.bind(this, view);

        distanceToPathTextView.setText(distaneToPathText + sharedPref.getDistaneToPath() + "[ m ]");
        gpsRefresTextView.setText(gpsRefreshText + ((float) sharedPref.getGpsRefresh()) + "[ s ]");
        distanceToPoiTextView.setText(distaneToPoiText + sharedPref.getDistaneToPoi() + "[ m ]");

        distaneToPathSeekBar.setProgress((int) sharedPref.getDistaneToPath());
        distaneToPoiSeekBar.setProgress((int) sharedPref.getDistaneToPoi());
        gpsRefreshSeekBar.setProgress((int) sharedPref.getGpsRefresh());

        showPoiCircle = sharedPref.getPoiCircle();
        showPathCircle = sharedPref.getPathCircle();

        distanceToPath();
        distaceToPoi();
        gpsRefresh();
        raddioButtonListnere();
        poiRadioButton.setChecked(showPoiCircle);
        distaneToRouteRadioButton.setChecked(showPathCircle);
        return view;

    }

    private void raddioButtonListnere() {

        poiRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoiCircle = !showPoiCircle;
                poiRadioButton.setChecked(showPoiCircle);
                sharedPref.putPoiCircle(showPoiCircle);

            }
        });

        distaneToRouteRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPathCircle = !showPathCircle;
                distaneToRouteRadioButton.setChecked(showPathCircle);
                sharedPref.putPathCircle(showPathCircle);
            }
        });

    }

    private void distaceToPoi() {

        distaneToPoiSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int distaneToPoiSeekBarProgress = distaneToPoiSeekBar.getProgress();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distaneToPoiSeekBarProgress = progress;
                distanceToPoiTextView.setText(distaneToPoiText + progress + "[ m ]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                sharedPref.putDistaneToPoi(distaneToPoiSeekBarProgress);
            }
        });

    }

    private void gpsRefresh() {

        gpsRefreshSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int refrestProgres = gpsRefreshSeekBar.getProgress();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refrestProgres = progress;

                gpsRefresTextView.setText(gpsRefreshText + ((float) progress) + " [ s ]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                sharedPref.putGpsRefresh(refrestProgres);
            }
        });
    }

    private void distanceToPath() {

        distaneToPathSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int distaneToPathProgres = distaneToPathSeekBar.getProgress();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distaneToPathProgres = progress;
                distanceToPathTextView.setText(distaneToPathText + progress + " [ m ]");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distanceToPathTextView.setText(distaneToPathText + distaneToPathProgres + " [ m ]");
                sharedPref.putDistaneToPath(distaneToPathProgres);
            }
        });
    }

}
