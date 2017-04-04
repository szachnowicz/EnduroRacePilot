package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PointOfInterest;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.MapFragmentPOI;
import com.pwr.projekt.enduroracepilot.interfaces.OnSelectedPOIListener;

public class MapFragmentActivity extends AppCompatActivity implements OnSelectedPOIListener {

    private FragmentManager fragmentManager;
    private MapFragmentPOI firstFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);
        fragmentManager = getSupportFragmentManager();
         transaction = fragmentManager.beginTransaction();
        firstFragment = new MapFragmentPOI();
        transaction.add(R.id.mapFragment, firstFragment);
        transaction.commit();

        //ViewGroup.LayoutParams params = firstFragment.getView().getLayoutParams();
//        params.height = 900;

    }

    @Override
    public void buttonClick() {

        firstFragment.getView().setVisibility(View.GONE);
        Toast.makeText(this, "this is toast form Actvity", Toast.LENGTH_SHORT).show();
        transaction.commit();
    }
}
