package com.example.tripaya.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.tripStartPoint.setText(tripClass.getStartPoint());
        holder.tripEndPoint.setText(tripClass.getEndPoint());
        holder.tripDate.setText(tripClass.getDate());
        holder.tripTime.setText(tripClass.getTime());
        holder.tripType.setText(tripClass.getTripType());
        holder.tripStatus.setText(tripClass.getTripStatus());

        holder.tripNotes.setOnClickListener(v -> {

            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.show_notes_dialog);
            int width = WindowManager.LayoutParams.MATCH_PARENT;
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width,height);
            dialog.show();

            TextView textView = dialog.findViewById(R.id.tv_notes);
            textView.setText(tripClass.getNote());
            Toast.makeText(v.getContext(), "Notes", Toast.LENGTH_SHORT).show();
        });

        holder.itemOption.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.itemOption);
            popupMenu.inflate(R.menu.item_option);

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_item_start:
                        Toast.makeText(v.getContext(), "Started", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_item_cancel:
                        Toast.makeText(v.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
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
        private ImageButton itemOption;
        private TextView tripStatus;
        private ImageButton tripNotes;


        public TripHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tv_trip_name);
            tripStartPoint = itemView.findViewById(R.id.tvStartPoint);
            tripEndPoint = itemView.findViewById(R.id.tvEndPoint);
            tripDate = itemView.findViewById(R.id.tv_date_picker);
            tripTime = itemView.findViewById(R.id.tv_time_picker);
            tripType = itemView.findViewById(R.id.tv_trip_type);
            itemOption = itemView.findViewById(R.id.image_button_option);
            tripStatus = itemView.findViewById(R.id.tv_status);
            tripNotes = itemView.findViewById(R.id.image_button_notes);



            itemView.setOnClickListener(view -> {
                // we need get the position of the item clicked
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(trips.get(position));
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
