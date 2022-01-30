package com.example.tripaya.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TripHolder> {
    private List<TripClass> trips = new ArrayList<>();
    private TripAdapter.OnItemClickListener listener;
    private TripAdapter.status status;
    private Context context;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public HistoryAdapter() {
    }


    @NonNull
    @Override
    public HistoryAdapter.TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_history, parent, false);


        return new HistoryAdapter.TripHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.TripHolder holder, int position) {
        // this take care to take the data from single node into views in tripHolder
        TripClass tripClass = trips.get(position);
        holder.tripName.setText(tripClass.getTripName());
        holder.tripStartPoint.setText(tripClass.getStartPoint());
        holder.tripEndPoint.setText(tripClass.getEndPoint());
        holder.tripDate.setText(tripClass.getDate());
        holder.tripTime.setText(tripClass.getTime());
        holder.tripType.setText(tripClass.getTripType());
        holder.tripStatus.setText(tripClass.getTripStatus());

        holder.imageButton.setOnClickListener(v -> {

            String sSource = holder.tripStartPoint.getText().toString().trim();
            String sDestination = holder.tripEndPoint.getText().toString().trim();
            if (sSource.equals("") && sDestination.equals("")) {
                Toast.makeText(v.getContext(), "Please Enter your start and end location", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            }
        });
        holder.itemOption.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.itemOption);
            popupMenu.inflate(R.menu.item_option_history);

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_item_cancel:
                        Dialog dialog = new Dialog(v.getContext());
                        dialog.setContentView(R.layout.show_notes_dialog);
                        int width = WindowManager.LayoutParams.MATCH_PARENT;
                        int height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setLayout(width, height);
                        dialog.show();
                        TextView textView = dialog.findViewById(R.id.tv_notes);
                        textView.setText(tripClass.getNote());
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
        private ImageButton itemOption,imageButton;
        private TextView tripStatus;

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tv_trip_name);
            tripStartPoint = itemView.findViewById(R.id.tvStartPoint);
            tripEndPoint = itemView.findViewById(R.id.tvEndPoint);
            tripDate = itemView.findViewById(R.id.tv_date_picker);
            tripTime = itemView.findViewById(R.id.tv_time_picker);
            tripType = itemView.findViewById(R.id.tv_trip_type);
            tripStatus = itemView.findViewById(R.id.tv_status);
            imageButton = itemView.findViewById(R.id.imageButton2);
            itemOption = itemView.findViewById(R.id.image_button_option);

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

    public void OnItemClickListener(TripAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
