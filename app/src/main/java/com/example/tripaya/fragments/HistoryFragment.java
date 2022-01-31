package com.example.tripaya.fragments;


import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaya.R;
import com.example.tripaya.adapter.HistoryAdapter;
import com.example.tripaya.adapter.TripAdapter;
import com.example.tripaya.fragments.helpers.DataParser;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.HistoryViewModel;
import com.example.tripaya.viewmodel.TripViewModel;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HistoryFragment extends Fragment implements OnMapReadyCallback {

    private RecyclerView recyclerViewHistory;
    //create object of viewModel
    private HistoryViewModel historyViewModel;
    private GoogleMap mMap;
    ArrayList markerPoints = new ArrayList();
    HistoryAdapter tripAdapter = new HistoryAdapter();

    /*    LatLng locationS = new LatLng(22.56452, 46.123213);

    LatLng locationE = new LatLng(24.56452, 48.123213);*/
    View view;

    private int[] colors={
            Color.RED,
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.DKGRAY,
            Color.GRAY,
            Color.GREEN,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.TRANSPARENT,
            Color.WHITE,
            Color.YELLOW
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragmentId);
        mapFragment.getMapAsync(this);
        initComponent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initRecycler();
    }
    private void initComponent() {
        recyclerViewHistory = view.findViewById(R.id.recycler_view_history);
    }

    private void initRecycler() {
        // initialize recycler view with TripAdapter
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewHistory.setHasFixedSize(true);
        recyclerViewHistory.setAdapter(tripAdapter);

        historyViewModel = new ViewModelProvider(getActivity()).get(HistoryViewModel.class);


        // this method observe the data if any thing change
        historyViewModel.getAllTripsCompleted().observe(getActivity(), tripClasses -> {

           if (tripClasses.isEmpty()) {
                historyViewModel.setFireCompleted();
            }
            // onChanged is called when the activity on the foreground
            //update recycler view
            tripAdapter.setTrips(tripClasses);
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT |
                        ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // to getPosition of the item and delete it
                // if we want delete the item inside viewModel we need the item not position of the item
                // we set method return position depend on the position in the adapter
                historyViewModel.delete(tripAdapter.getTripAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Trip Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewHistory);

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.upcoming_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        String errorMessage = "";

        mMap = googleMap;

        for (TripClass trip : tripAdapter.getTrips()) {
            LatLng origin ;
            LatLng dest ;

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try{
                Address startPoint = geocoder.getFromLocationName(trip.getStartPoint(),1).get(0);
                origin = new LatLng(startPoint.getLatitude(),startPoint.getLongitude());

                Address endPoint = geocoder.getFromLocationName(trip.getEndPoint(),1).get(0);
                dest = new LatLng(startPoint.getLatitude(),startPoint.getLongitude());
            }catch (Exception exception){
                errorMessage = exception.getMessage();
                continue;
            }




            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 10));

            // Creating MarkerOptions
            MarkerOptions options1 = new MarkerOptions();
            MarkerOptions options2 = new MarkerOptions();

            // Setting the position of the marker
            options1.position(origin);
            options2.position(dest);

            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options1);
            mMap.addMarker(options2);

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDztAjcgoolhK_1EtCISqxjf2cBA33tk0Q";

        return url;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            int color = new Random().nextInt(colors.length-1);
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            if(result == null)
                return;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(colors[color]);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
}

