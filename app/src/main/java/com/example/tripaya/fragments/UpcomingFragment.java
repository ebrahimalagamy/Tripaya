package com.example.tripaya.fragments;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tripaya.AddTripActivity;
import com.example.tripaya.InternetConnection;
import com.example.tripaya.R;
import com.example.tripaya.SimpleService;
import com.example.tripaya.adapter.TripAdapter;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.TripViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.siddharthks.bubbles.FloatingBubblePermissions;


public class UpcomingFragment extends Fragment {
    public static final String TAG = "Trace Location";
    FusedLocationProviderClient client;
    TripClass tripClass;
    View view;
    ConstraintLayout constrain;
    FloatingActionButton floatingActionButton;
    int y = 1;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    TripAdapter tripAdapter;
    private RecyclerView recyclerViewTrip;
    //create object of viewModel
    private TripViewModel tripViewModel;

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
        onScroll();
      //  FloatingBubblePermissions.startPermissionRequest(getActivity());
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
        coordinatorLayout = getActivity().findViewById(R.id.coordinator);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTrip.setLayoutManager(layoutManager);
        recyclerViewTrip.setHasFixedSize(true);

        TripAdapter tripAdapter = new TripAdapter(getContext(), this::onTripStart);
        recyclerViewTrip.setAdapter(tripAdapter);

        tripViewModel = new ViewModelProvider(getActivity()).get(TripViewModel.class);
        // this method observe the data if any thing change
        tripViewModel.getAllTrips().observe(requireActivity(), tripClasses -> {
            tripViewModel.getIsSignedOut().observe(getActivity(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (tripClasses.isEmpty() && !aBoolean) {
                        tripViewModel.setFireBaseTrips();
                    } else {
                        tripAdapter.setTrips(tripClasses);
                    }
                    // onChanged is called when the activity on the foreground
                    //update recycler view
                }
            });

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
                int pos = viewHolder.getAdapterPosition();
                TripClass trip = tripAdapter.getTripAt(pos);
                // to getPosition of the item and delete it
                // if we want delete the item inside viewModel we need the item not position of the item
                // we set method return position depend on the position in the adapter
                tripViewModel.delete(tripAdapter.getTripAt(viewHolder.getAdapterPosition()));
                /*if (pos == 0) {
                    tripAdapter.clearFocus();
                }*/


                Toast.makeText(getActivity(), "Trip Deleted", Toast.LENGTH_SHORT).show();
           //     snackbar(pos, trip);
                tripAdapter.notifyItemRemoved(pos);

            }
        }).attachToRecyclerView(recyclerViewTrip);

        // when i click on item then send info to add trip activity to edit it
        tripAdapter.OnItemClickListener(tripClass -> {
            if (InternetConnection.checkConnection(getContext())) {
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
            } else {
                Toast.makeText(getContext(), "plz check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
        tripAdapter.onStatesChangeListner(tripClass -> {
            tripViewModel.update(tripClass);
        });

    }

    private void snackbar(final int pos, final TripClass tripClass) {
        snackbar = Snackbar.make(coordinatorLayout, "item deleted",
                BaseTransientBottomBar.LENGTH_LONG).addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // undo = 1;

                        Toast.makeText(requireContext(), "recoverd", Toast.LENGTH_SHORT).show();
                        tripAdapter.restoreItem(tripClass, pos);
                        tripViewModel.insert(tripClass);
                        recyclerViewTrip.scrollToPosition(pos);


                    }
                });

            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
//
            }
        });
        snackbar.show();

    }

    private void onScroll() {

        constrain = getActivity().findViewById(R.id.bottom_Nav_Bar);
        floatingActionButton = getActivity().findViewById(R.id.fab_button);

        recyclerViewTrip.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean state = false;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println(newState + " state");
//
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                constrain.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);
                //System.out.println(dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;
                System.out.println(endHasBeenReached);
                if (totalItemCount > 5 && endHasBeenReached && !state) {
                    YoYo.with(Techniques.SlideOutDown)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    constrain.setVisibility(View.GONE);

                                }
                            })
                            .playOn(constrain);

                    YoYo.with(Techniques.SlideOutDown)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    floatingActionButton.setVisibility(View.GONE);

                                }
                            })
                            .playOn(floatingActionButton);
                    state = true;
                }
                if (!endHasBeenReached && state) {
                    YoYo.with(Techniques.SlideInUp)
                            .duration(400)
                            .playOn(constrain);
                    constrain.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.SlideInUp)
                            .duration(400)
                            .playOn(floatingActionButton);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    y = 1;
                    state = false;
                }
            }


        });
    }

    public void onTripStart(TripClass tripClass) {
        this.tripClass = tripClass;
        tripClass.setTripStatus("Completed");
        tripViewModel.update(tripClass);
        onTripStart();
        //getContext().startService(new Intent(getActivity(), SimpleService.class));
    }

    public void onTripStart() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        // WorkManagerRepo.setWorkers(getContext(),tripClasses);
        client = LocationServices.getFusedLocationProviderClient(getContext());

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                String latLng = location.getLatitude() + "," + location.getLongitude();
                String sDestination = tripClass.getEndPoint().trim();

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

        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                tripViewModel.deleteAllTripsall();
                Toast.makeText(getActivity(), "All trips deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}