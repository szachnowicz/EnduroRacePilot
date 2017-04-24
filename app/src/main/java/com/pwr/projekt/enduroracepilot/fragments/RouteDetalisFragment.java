package com.pwr.projekt.enduroracepilot.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pwr.projekt.enduroracepilot.MVP.presenter.BrowseRoutePresenter;
import com.pwr.projekt.enduroracepilot.MVP.view.RouteView;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.RoutePointsAdapter;
import com.pwr.projekt.enduroracepilot.interfaces.RouteDetalisCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetalisFragment extends Fragment implements RouteView {

    @BindView(R.id.pointsListView)
    ListView pointsListView;
    @BindView(R.id.progressBarRouteDetalis)
    ProgressBar progressBar;
    @BindView(R.id.textViewRouteDetalis)
    TextView routeDealis;

    private BrowseRoutePresenter browseRoutePresenter;

    private View view;

    private String ROUTE_ID_REFERENCE_KEY;

    private RoutePointsAdapter adapter;

    private Route route;

    private RouteDetalisCallback routeCreationCallback;

    public RouteDetalisFragment() {

    }

    public static Fragment newInstance(String _ROUTE_ID_REFERENCE_KEY) {
        RouteDetalisFragment fragment = new RouteDetalisFragment();
        fragment.setRouteRefernce(_ROUTE_ID_REFERENCE_KEY);

        return fragment;
    }

    private void setRouteRefernce(String _ROUTE_ID_REFERENCE_KEY) {
        this.ROUTE_ID_REFERENCE_KEY = _ROUTE_ID_REFERENCE_KEY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_points_of_route, container, false);

        ButterKnife.bind(this, view);
        route = new Route();
        browseRoutePresenter = new BrowseRoutePresenter(this);
        browseRoutePresenter.getAllData();
        adapter = new RoutePointsAdapter(getContext(), route.getPointsOfRoute());
        pointsListView.setAdapter(adapter);

        return view;

    }

    @OnItemClick(R.id.pointsListView)
    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
        routeCreationCallback.itemOnListCliked(route.getPointsOfRoute().get(postion));
    }

    @OnItemLongClick(R.id.pointsListView)
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

        createDeletePointBulider(position);

        return true;
    }

    private void createDeletePointBulider(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyDataSetChanged();
                browseRoutePresenter.removePointFromRoute(route, position);
                routeCreationCallback.passTheRouteMapFragment(route);
                routeCreationCallback.itemOnListCliked(route.getPointsOfRoute().get(position));

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
    }

    @Override
    public void displayRouteDetalis(List<Route> list) {
//        route = list.stream().filter(s -> s.getRouteID().equals(ROUTE_ID_REFERENCE_KEY)).findFirst().get();
        for (Route x : list) {
            if (x.getRouteID().equals(ROUTE_ID_REFERENCE_KEY)) {
                route = x;
                break;
            }
        }

        adapter = new RoutePointsAdapter(getContext(), route.getPointsOfRoute());
        routeDealis.setText(browseRoutePresenter.prepareRouteDetalis(route).toString());
        pointsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        routeCreationCallback.passTheRouteMapFragment(route);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {

                routeCreationCallback = (RouteDetalisCallback) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                                                     + " must implement OnSelected");

            }
        }
    }

    public void setRoute(Route _route) {
        route = _route;
        adapter.notifyDataSetChanged();
        routeDealis.setText(browseRoutePresenter.prepareRouteDetalis(route).toString());
    }

    public void saveRoute() {
        browseRoutePresenter.saveRoute(route);
    }
}
