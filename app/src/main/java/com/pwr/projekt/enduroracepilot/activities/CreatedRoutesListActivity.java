package com.pwr.projekt.enduroracepilot.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.ArrayList;

// TODO: 2017-04-18 ciekawe rzeczy
//https://github.com/wasabeef/awesome-android-ui/blob/master/pages/Progress.md
public class CreatedRoutesListActivity extends AppCompatActivity {

    public static final String ROUTE_ID = "ROUTE_REFERENCE";
    private ListView createdRouteListview;
    private Button newRoute;
    private ArrayList<Route> routeList;
    private ArrayAdapter<Route> adapter;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference routeRefernce = FirebaseDatabase.getInstance().getReference().child(Route.TABEL_NAME);
        getAllResults(Route.TABEL_NAME);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_routes);
        routeList = new ArrayList<>();
        final Intent creatRoute = new Intent(this, RouteCreatingActivity.class);
        createdRouteListview = (ListView) findViewById(R.id.routeListARA);


        adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeList);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);




        createdRouteListview.setAdapter(adapter);
        createdRouteListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                creatRoute.putExtra(ROUTE_ID, routeList.get(postion).get_routeID());
                startActivity(creatRoute);

            }
        });

        createdRouteListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CreatedRoutesListActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Database().getRefereceToObject(Route.TABEL_NAME, routeList.get(position).get_routeID())
                                .removeValue();
                        routeList.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

        newRoute = (Button) findViewById(R.id.createRouteButton);
        newRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatedRoutesListActivity.this, AddingSingelRouteActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getAllResults(String child) {
        new Database().readDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE


            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snap : children
                        ) {

                    Route value = snap.getValue(Route.class);
                    if (!routeList.contains(value)) {
                        routeList.add(value);
                    }
                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }
}
