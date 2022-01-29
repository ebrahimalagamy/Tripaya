package com.example.tripaya;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import androidx.lifecycle.Observer;
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
    private TextView tvDate, tvTime, tvStatus;
    private EditText etStartPoint, etEndPoint, etTripName, etNotes;
    private int switchTagEditText = -1;
    private Button btnSaveTrip, btnStartTrip;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private AddTripViewModel addTripViewModel;

    public static final String ID = "com.example.tripaya.id";
    public static final String NAME = "com.example.tripaya.name";
    public static final String START = "com.example.tripaya.start";
    public static final String END = "com.example.tripaya.end";
    public static final String DATE = "com.example.tripaya.date";
    public static final String TIME = "com.example.tripaya.time";
    public static final String NOTE = "com.example.tripaya.note";
    // public static final String TYPE = "com.example.tripaya.type";

    private String tripType;
    private boolean editMode;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);


        initComponent();
        initListener();
        //initialize places
        Places.initialize(getApplicationContext(), API_KEY);

        Intent intent = getIntent();
        if (intent.hasExtra(ID)) {
            // Details trip
            setTitle("Trip Details");
            btnStartTrip.setVisibility(View.VISIBLE);
            btnStartTrip.setText("Start");

            editMode = true;
            btnSaveTrip.setText(R.string.string_btn_save_trip_switch);
            mId = intent.getIntExtra(ID, -1);
            //setBtnEnables();
            etTripName.setText(intent.getStringExtra(NAME));
            etStartPoint.setText(intent.getStringExtra(START));
            etEndPoint.setText(intent.getStringExtra(END));
            tvDate.setText(intent.getStringExtra(DATE));
            tvTime.setText(intent.getStringExtra(TIME));
            etNotes.setText(intent.getStringExtra(NOTE));

            // radioButton.setText(intent.getStringExtra(TYPE));
        } else {
            // add trip
            setTitle("Add Trip");
            editMode = false;
            btnSaveTrip.setText(R.string.string_btn_Add_trip_switch);
        }
        addTripViewModel = new ViewModelProvider(this).get(AddTripViewModel.class);

    }

   /* private void setBtnEnables() {
        etTripName.setEnabled(false);
        etStartPoint.setEnabled(false);
        etEndPoint.setEnabled(false);
        tvDate.setEnabled(false);
        tvTime.setEnabled(false);

    }*/

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
        String tripNotes = etNotes.getText().toString().trim();
        //String tripStatus = tvStatus.getText().toString().trim();

        int tripPosition = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(tripPosition);
        tripType = radioButton.getText().toString().trim();

        TripClass tripClass = new TripClass();
        addTripViewModel.getAllTrips().observe(this, new Observer<List<TripClass>>() {
            @Override
            public void onChanged(List<TripClass> tripClasses) {
                if(tripClasses.isEmpty())
                {
                    tripClass.setId(1);
                }else{
                    tripClass.setId(tripClasses.size()+1);
                }
                if (editMode) {
                    tripClass.setId(mId);
                    addTripViewModel.update(tripClass);
                } else {
                    if (tripName.isEmpty() || tripStartPoint.isEmpty() || tripEndPoint.isEmpty() || tripDate.isEmpty() ||
                            tripTime.isEmpty() || tripType.isEmpty()) {
                        addTripViewModel.getAllTrips().removeObserver(this);
                        return;
                    }
                    addTripViewModel.insert(tripClass);
                }
                finish();
                addTripViewModel.getAllTrips().removeObserver(this);

            }
        });


        tripClass.setTripName(tripName);
        tripClass.setStartPoint(tripStartPoint);
        tripClass.setEndPoint(tripEndPoint);
        tripClass.setDate(tripDate);
        tripClass.setTime(tripTime);
        tripClass.setTripType(tripType);
        tripClass.setTripStatus("Upcoming");
        tripClass.setNote(tripNotes);

        Log.e("addTripActivity", "Trip ID: "+tripClass.getId());
        if (tripName.isEmpty() || tripStartPoint.isEmpty() || tripEndPoint.isEmpty() || tripDate.isEmpty() ||
                tripTime.isEmpty() || tripType.isEmpty()) {
            Toast.makeText(this, "Please Enter All the Information", Toast.LENGTH_SHORT).show();
            return;
        }
        // insert trip in database and close this activity


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // if operation success initialize places
            Place place = Autocomplete.getPlaceFromIntent(data);

            if (switchTagEditText == 0) {
                etStartPoint.setText(place.getName());
            } else if (switchTagEditText == 1) {
                etEndPoint.setText(place.getName());
            }

            //set location name
            String location = String.format("Location Address : %s", place.getName());
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
        tvStatus = findViewById(R.id.tv_status);
        btnStartTrip = findViewById(R.id.btnStart);
        etNotes = findViewById(R.id.et_notes);
        // to get button checked
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
        tvTime.setText(hour + " : " + minute);
    }
}