package com.pwr.projekt.enduroracepilot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.Date;

public class AddingRouteActivity extends AppCompatActivity {

    public static final String ROUTE_ID = "ROUTE_REFERENCE";
    private TextView routeName;
    private TextView routeDiscription;
    private TextView routeAuthor;

    private DatabaseReference routeDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        routeName = (TextView) findViewById(R.id.routeNameTextView);
        routeAuthor = (TextView) findViewById(R.id.authorOfRouteEditText);
        routeDiscription = (TextView) findViewById(R.id.routeDiscriptionEditText);

    }

    public void createRoute(View view) {

        if (!checkIfFullFilledForm()) {
            return;
        }
        routeDatabaseReference = FirebaseDatabase.getInstance().getReference(Route.TABEL_NAME);
        String routeID = routeDatabaseReference.push().getKey();

        Route route = new Route();
        route.set_routeID(routeID);
        route.setRouteName(routeName.getText().toString());
        route.setAuthor(routeAuthor.getText().toString());
        route.setRouteDiscription(routeDiscription.getText().toString());
        route.setDate(new Date());

        routeDatabaseReference.child(routeID).setValue(route);
        Intent creatRoute = new Intent(this, RouteCreatingActivity.class);
        creatRoute.putExtra(ROUTE_ID, routeID);

        startActivity(creatRoute);

    }

    private boolean checkIfFullFilledForm() {
        if (routeDiscription.getText().length() < 1) {
            Toast.makeText(this, "Pole z opisem trasy jest puste !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (routeAuthor.getText().length() < 1) {
            Toast.makeText(this, "Pole z autore trasy jest puste !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (routeName.getText().length() < 1) {
            Toast.makeText(this, "Pole z Nazwa trasy jest puste !", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
