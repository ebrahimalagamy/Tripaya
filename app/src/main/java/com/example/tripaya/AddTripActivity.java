package com.example.tripaya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.tripaya.date.DatePickerFragment;
import com.example.tripaya.date.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {

    private ImageButton imageButtonCalender, imageButtonTime;
    private TextView tvCalender,tvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        initComponent();
        initListener();
    }

    private void initListener() {
        // click buttonImage to Call DatePickerFragment
        imageButtonCalender.setOnClickListener(view -> {
            DatePickerFragment pickerFragment = new DatePickerFragment();
            pickerFragment.show(getSupportFragmentManager(),"Date Picker");
        });
        // click buttonImage to Call TimePickerFragment
        imageButtonTime.setOnClickListener(view -> {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(),"Time Picker");
        });
    }

    private void initComponent() {
        imageButtonCalender = findViewById(R.id.image_button_calender);
        imageButtonTime = findViewById(R.id.image_button_time);
        tvCalender = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
    }

    // this method to set date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvCalender.setText(dateFormat);

    }
    // this method to set time
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        tvTime.setText(hour+":"+minute);

    }
}