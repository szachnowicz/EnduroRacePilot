package com.pwr.projekt.enduroracepilot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;
import com.pwr.projekt.enduroracepilot.model.SharedPref;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddingSingelRouteActivity extends AppCompatActivity {

    public static final String ROUTE_ID = "ROUTE_REFERENCE";
    @BindView(R.id.routeNameTextView)
    TextView routeName;
    @BindView(R.id.authorOfRouteEditText)
    TextView routeDiscription;
    @BindView(R.id.routeDiscriptionEditText)
    TextView routeAuthor;

    @BindView(R.id.autorTextInputLayoutASR)
    TextInputLayout authorTextInputLayout;

    @BindView(R.id.routeDiscTextInputLayoutASR)
    TextInputLayout routeDiscriptionInputLayout;

    @BindView(R.id.routeTextInputLayoutASR)
    TextInputLayout routeInputLayout;

    private DatabaseReference routeDatabaseReference;
    private SharedPref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);
        ButterKnife.bind(this);
        sharePref = new SharedPref(getApplicationContext());

//        routeName = (TextView) findViewById(R.id.routeNameTextView);
//        routeAuthor = (TextView) findViewById(R.id.authorOfRouteEditText);
//        routeDiscription = (TextView) findViewById(R.id.routeDiscriptionEditText);

    }

    @OnClick(R.id.createNewRouteButton)
    public void createRoute(View view) {

        if (!checkIfFullFilledForm()) {
            return;
        }
        routeDatabaseReference = FirebaseDatabase.getInstance().getReference(Route.TABEL_NAME);
        String routeID = routeDatabaseReference.push().getKey();

        Route route = new Route();
        route.setRouteID(routeID);
        route.setRouteName(routeName.getText().toString());
        route.setAuthor(sharePref.getUserId());
        route.setRouteDiscription(routeDiscription.getText().toString());
        route.setDate(new Date());

        routeDatabaseReference.child(routeID).setValue(route);
        Intent creatRoute = new Intent(this, RouteCreatingActivity.class);
        creatRoute.putExtra(ROUTE_ID, routeID);

        startActivity(creatRoute);

    }

    private boolean checkIfFullFilledForm() {

//        if (routeAuthor.getText().length() < 1) {
//            authorTextInputLayout.setError("Pole z authorem trasy nie może byc puste! !");
//            return false;
//        }

        if (routeName.getText().length() < 1) {
            routeInputLayout.setError("Podaje nazwe trasy");
            return false;
        }

        return true;
    }

}
