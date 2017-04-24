package com.pwr.projekt.enduroracepilot.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.pwr.projekt.enduroracepilot.MVP.presenter.BrowseRoutePresenter;
import com.pwr.projekt.enduroracepilot.MVP.view.BrowseRouteView;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

// TODO: 2017-04-18 ciekawe rzeczy
//https://github.com/wasabeef/awesome-android-ui/blob/master/pages/Progress.md
public class BrowseRouteActivity extends AppCompatActivity implements BrowseRouteView {

    public static final String ROUTE_ID = "ROUTE_REFERENCE";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.routeListARA)
    ListView createdRouteListview;

    private BrowseRoutePresenter browseRoutePresenter;
    private ArrayList<Route> routeList;
    private ArrayAdapter<Route> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_routes);

        ButterKnife.bind(this);
        browseRoutePresenter = new BrowseRoutePresenter(this);
        browseRoutePresenter.getAllData();
        setUpGuiStaff();

    }

    private void setUpGuiStaff() {
        routeList = new ArrayList<>();
        adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeList);
        createdRouteListview.setAdapter(adapter);
    }

    @OnItemLongClick(R.id.routeListARA)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        return createDeleteRouteDialog(position);
    }
    @OnClick(R.id.createRouteButton)
    public void onClick(){

            Intent mapActivity = new Intent(this, RouteCreatingActivity.class);
            startActivity(mapActivity);

    }
    @OnItemClick(R.id.routeListARA)
    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
        final Intent creatRoute = new Intent(this, RouteCreatingActivity.class);
        creatRoute.putExtra(ROUTE_ID, routeList.get(postion).getRouteID());

        startActivity(creatRoute);

    }

    @Override
    public void displayRouteDetalis(List<Route> list) {
        routeList.addAll(list);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    private boolean createDeleteRouteDialog(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(BrowseRouteActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                browseRoutePresenter.removeRoute(routeList.get(position));
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

}
