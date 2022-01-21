package com.example.tripaya.fragments;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaya.R;
import com.example.tripaya.adapter.TripAdapter;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.TripViewModel;

import java.util.List;


public class UpcomingFragment extends Fragment {
    private RecyclerView recyclerViewTrip;
    //create object of viewModel
    private TripViewModel tripViewModel;
    View view;

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

    private void initComponent() {
        recyclerViewTrip = view.findViewById(R.id.recycler_view_trip);
    }

    private void initRecycler() {
        // initialize recycler view with TripAdapter
        recyclerViewTrip.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTrip.setHasFixedSize(true);
        TripAdapter tripAdapter = new TripAdapter();
        recyclerViewTrip.setAdapter(tripAdapter);

        tripViewModel = new ViewModelProvider(getActivity()).get(TripViewModel.class);
        // this method observe the data if any thing change
        tripViewModel.getAllTrips().observe(getActivity(), new Observer<List<TripClass>>() {
            @Override
            public void onChanged(List<TripClass> tripClasses) {
                // onChanged is called when the activity on the foreground
                //update recycler view
                tripAdapter.setTrips(tripClasses);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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

            }
        }).attachToRecyclerView(recyclerViewTrip);
        tripAdapter.serOnItemClickListener(new TripAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TripClass tripClass) {


            }
        });
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