package com.example.tripaya.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaya.AddTripActivity;
import com.example.tripaya.Alert.WorkManagerRepo;
import com.example.tripaya.R;
import com.example.tripaya.adapter.TripAdapter;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.TripViewModel;
<<<<<<< Updated upstream
=======
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.siddharthks.bubbles.FloatingBubblePermissions;
>>>>>>> Stashed changes

public class UpcomingFragment extends Fragment {
    private RecyclerView recyclerViewTrip;
    //create object of viewModel
    private TripViewModel tripViewModel;
    FusedLocationProviderClient client;
    TripClass tripClass;
    View view;

    public static final String TAG = "Trace Location";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        initComponent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initRecycler();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Upcoming");
    }

    private void initComponent() {
        recyclerViewTrip = view.findViewById(R.id.recycler_view_trip);
    }

    private void initRecycler() {
        // initialize recycler view with TripAdapter
        recyclerViewTrip.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTrip.setHasFixedSize(true);
        TripAdapter tripAdapter = new TripAdapter(getContext(),this::onTripStart);
        recyclerViewTrip.setAdapter(tripAdapter);

        tripViewModel = new ViewModelProvider(getActivity()).get(TripViewModel.class);
        // this method observe the data if any thing change
        tripViewModel.getAllTrips().observe(getActivity(), tripClasses -> {

            if (tripClasses.isEmpty()) {
                tripViewModel.setFireBaseTrips();
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
                tripViewModel.delete(tripAdapter.getTripAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Trip Deleted", Toast.LENGTH_SHORT).show();
                //WorkManagerRepo.setWorkers(getContext(),);

            }
        }).attachToRecyclerView(recyclerViewTrip);

        // when i click on item then send info to add trip activity to edit it
        tripAdapter.OnItemClickListener(tripClass -> {

            Intent intent = new Intent(getActivity(), AddTripActivity.class);

            intent.putExtra(AddTripActivity.ID, tripClass.getId());
            intent.putExtra(AddTripActivity.NAME, tripClass.getTripName());
            intent.putExtra(AddTripActivity.START, tripClass.getStartPoint());
            intent.putExtra(AddTripActivity.END, tripClass.getEndPoint());
            intent.putExtra(AddTripActivity.DATE, tripClass.getDate());
            intent.putExtra(AddTripActivity.TIME, tripClass.getTime());
            intent.putExtra(AddTripActivity.NOTE, tripClass.getNote());
            // intent.putExtra(AddTripActivity.TYPE,tripClass.getTripType());
            startActivity(intent);

        });
        tripAdapter.onStatesChangeListner(tripClass -> {
            tripViewModel.update(tripClass);
        });
    }

    public void onTripStart(TripClass tripClass) {
        this.tripClass = tripClass;
        onTripStart();
    }

    public void onTripStart(){
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }
        WorkManagerRepo.setWorkers(getContext(),tripClasses);
        client = LocationServices.getFusedLocationProviderClient(getContext());

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                String latLng = location.getLatitude()+","+ location.getLongitude();
                String sDestination = tripClass.getEndPoint().toString().trim();

                if (latLng.equals("") && sDestination.equals("")) {
                    Toast.makeText(getContext(), "Please Enter your start and end location", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + latLng + "/" + sDestination);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.google.android.apps.maps");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 44){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                onTripStart();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.upcoming_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_trips:
                tripViewModel.deleteAllTrips();
                Toast.makeText(getActivity(), "All trips deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}