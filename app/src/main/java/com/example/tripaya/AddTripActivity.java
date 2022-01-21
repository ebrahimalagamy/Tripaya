package com.example.tripaya;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripaya.datapicker.DatePickerFragment;
import com.example.tripaya.datapicker.TimePickerFragment;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.AddTripViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {

    private static final int REQUEST_CODE = 100;
    private static final String API_KEY = "AIzaSyDztAjcgoolhK_1EtCISqxjf2cBA33tk0Q";
    private ImageButton imageButtonCalender, imageButtonTime;
    private TextView tvDate, tvTime;
    private EditText etStartPoint, etEndPoint, etTripName;
    private int switchTagEditText = -1;
    private Button btnSaveTrip;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private AddTripViewModel addTripViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        initComponent();
        initListener();
        //initialize places
        Places.initialize(getApplicationContext(), API_KEY);
        addTripViewModel = new ViewModelProvider(this).get(AddTripViewModel.class);
    }

    private void initListener() {
        // click buttonImage to Call DatePickerFragment
        imageButtonCalender.setOnClickListener(view -> {
            DatePickerFragment pickerFragment = new DatePickerFragment();
            pickerFragment.show(getSupportFragmentManager(), "Date Picker");
        });
        // click buttonImage to Call TimePickerFragment
        imageButtonTime.setOnClickListener(view -> {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "Time Picker");
        });

        etStartPoint.setFocusable(false);
        etStartPoint.setOnClickListener(view -> {
            switchTagEditText = 0;
            // initialize place field list
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.NAME);
            //create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(getApplicationContext());

            // start activity for result
            startActivityForResult(intent, REQUEST_CODE);
        });
        etEndPoint.setFocusable(false);
        etEndPoint.setOnClickListener(view -> {
            switchTagEditText = 1;
            // initialize place field list
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.NAME);
            //create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(getApplicationContext());

            // start activity for result
            startActivityForResult(intent, REQUEST_CODE);
        });

        btnSaveTrip.setOnClickListener(view -> saveTrip());

    }
    private void saveTrip() {
        String tripName = etTripName.getText().toString().trim();
        String tripStartPoint = etStartPoint.getText().toString().trim();
        String tripEndPoint = etEndPoint.getText().toString().trim();
        String tripDate = tvDate.getText().toString().trim();
        String tripTime = tvTime.getText().toString().trim();
        String tripType = radioButton.getText().toString().trim();

        if (tripName.isEmpty() ||tripStartPoint.isEmpty() ||tripEndPoint.isEmpty() ||tripDate.isEmpty() ||
                tripTime.isEmpty()|| tripType.isEmpty() ){
            Toast.makeText(this, "Please Enter All the Information", Toast.LENGTH_SHORT).show();
            return;
        }
        // insert trip in database and close this activity
        addTripViewModel.insert(new TripClass(tripName,tripStartPoint,tripEndPoint,
                tripDate,tripTime,tripType));

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // if operation success initialize places
            Place place = Autocomplete.getPlaceFromIntent(data);

            if (switchTagEditText == 0) {
                etStartPoint.setText(place.getAddress());
            } else if (switchTagEditText == 1) {
                etEndPoint.setText(place.getAddress());
            }

            //set location name
            String location = String.format("Location name : %s", place.getName());
            Toast.makeText(getApplicationContext(), location, Toast.LENGTH_SHORT).show();
            // set latitude and longitude
            String latLong = String.valueOf(place.getLatLng());
            Toast.makeText(getApplicationContext(), latLong, Toast.LENGTH_SHORT).show();


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // when resultCode error initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void initComponent() {
        imageButtonCalender = findViewById(R.id.image_button_calender);
        imageButtonTime = findViewById(R.id.image_button_time);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        etTripName = findViewById(R.id.et_trip_name);
        etStartPoint = findViewById(R.id.et_start_point);
        etEndPoint = findViewById(R.id.et_end_point);
        btnSaveTrip = findViewById(R.id.btn_add_trip);
        radioGroup = findViewById(R.id.radio_group);
        // to get button checked
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioButtonId);

    }

    // this method to set date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvDate.setText(dateFormat);

    }

    // this method to set time
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        tvTime.setText(hour + ":" + minute);
    }
}