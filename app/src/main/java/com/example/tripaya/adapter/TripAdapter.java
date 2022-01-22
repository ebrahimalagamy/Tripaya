package com.example.tripaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaya.R;
import com.example.tripaya.roomdatabase.TripClass;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {
    private List<TripClass> trips = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        // this take care to take the data from single node into views in tripHolder
        TripClass tripClass = trips.get(position);
        holder.tripName.setText(tripClass.getTripName());
        holder.tripDate.setText(tripClass.getDate());
        holder.tripTime.setText(tripClass.getTime());
        holder.tripType.setText(tripClass.getTripType());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // if change configuration we need to load list of trips to recycler view
    public void setTrips(List<TripClass> trips) {
        this.trips = trips;
        // this will update recycler view after any change will change this method
        // TODO
        notifyDataSetChanged();
    }

    // here get the item depend on the position
    public TripClass getTripAt(int position) {
        return trips.get(position);
    }

    // holds the views
    class TripHolder extends RecyclerView.ViewHolder {
        private TextView tripName;
        // TODO later to add this text
        private TextView tripStartPoint;
        private TextView tripEndPoint;
        private TextView tripDate;
        private TextView tripTime;
        private TextView tripType;

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tv_trip_name);
            tripDate = itemView.findViewById(R.id.tv_date_picker);
            tripTime = itemView.findViewById(R.id.tv_time_picker);
            tripType = itemView.findViewById(R.id.tv_trip_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // we need get the position of the item clicked
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(trips.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TripClass tripClass);
    }

    public void OnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }
}
