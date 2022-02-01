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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaya.R;
import com.example.tripaya.roomdatabase.TripClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {
    OnItemClickListener runnable;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    private List<TripClass> trips = new ArrayList<>();
    private OnItemClickListener listener;
    private status status;
    private Context context;


    public TripAdapter(Context context) {
        this.context = context;
    }

    public TripAdapter(Context context, OnItemClickListener runnable) {
        this.context = context;
        this.runnable = runnable;
    }

    public TripAdapter() {
    }

    public void restoreItem(TripClass item, int position) {
        trips.add(position, item);

        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);

        client = LocationServices.getFusedLocationProviderClient(context);

        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        // this take care to take the data from single node into views in tripHolder
        TripClass tripClass = trips.get(position);
        holder.tripName.setText(tripClass.getTripName());
        holder.tripStartPoint.setText(tripClass.getStartPoint());
        holder.tripEndPoint.setText(tripClass.getEndPoint());



            String stdate = DateFormat.getDateInstance(DateFormat.FULL).format(tripClass.dateFromStringToDate());

            holder.tripDate.setText(stdate);




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


        holder.tripNotes.setOnClickListener(v -> {

            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.show_notes_dialog);
            int width = WindowManager.LayoutParams.MATCH_PARENT;
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
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
                        tripClass.setTripStatus("cancel");
                        status.onStatusChanged(tripClass);
                        // update(tripClass);
                        // holder.tripTime.setText("Cancel");

                        Toast.makeText(v.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            });
            popupMenu.show();
        });


        holder.btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                runnable.onItemClick(tripClass);
                /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    ActivityCompat.requestPermissions(, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    return;
                }



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
                }*/
            }
        });

               /*Intent intent= new Intent(context,SimpleService.class);
               // intent.setPackage("com.example.tripaya.fragments.");
                context.startService(intent);*/
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

    public void onStatesChangeListner(status status) {
        this.status = status;
    }

    public void OnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface status {
        void onStatusChanged(TripClass tripClass);
    }

    public interface OnItemClickListener {
        void onItemClick(TripClass tripClass);
    }

    public interface TimerStarter {
        void startTimer();
    }

    // holds the views
    class TripHolder extends RecyclerView.ViewHolder {
        private final TextView tripName;
        // TODO later to add this text
        private final TextView tripStartPoint;
        private final TextView tripEndPoint;
        private final TextView tripDate;
        private final TextView tripTime;
        private final TextView tripType;
        private final ImageButton itemOption;
        private final ImageButton imageButton;
        private final TextView tripStatus;
        private final ImageButton tripNotes;
        private final Button btnStartTrip;

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
            imageButton = itemView.findViewById(R.id.imageButton2);
            tripNotes = itemView.findViewById(R.id.image_button_notes);
            btnStartTrip = itemView.findViewById(R.id.btnStartTrip);

            itemView.setOnClickListener(view -> {
                // we need get the position of the item clicked
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(trips.get(position));
                }
            });
        }
    }

}
