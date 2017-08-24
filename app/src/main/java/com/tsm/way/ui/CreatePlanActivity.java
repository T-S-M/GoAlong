package com.tsm.way.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;
import com.tsm.way.R;

import java.util.Calendar;

public class CreatePlanActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreatePlanActivity";
    private static final int RESULT_LOAD_IMAGE = 14543;
    ImageView photo_up;
    Button img_upButton, createEventButton;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        mDisplayDate = (TextView) findViewById(R.id.datepick);
        mDisplayTime = (TextView) findViewById(R.id.timepick);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreatePlanActivity.this,
                        mDateSetListener,
                        year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        //set time
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);


                TimePickerDialog dialog = new TimePickerDialog(
                        CreatePlanActivity.this,
                        mTimeSetListener,
                        hour, minute, false);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String ampm;
                if (hour < 12) {
                    ampm = "AM";
                } else {
                    ampm = "PM";
                    hour -= 12;
                }

                Log.d(TAG, "onTimeSet: hour:minute " + hour + ":" + minute + ":" + ampm);
                String time = hour + ":" + minute + ":" + ampm;
                mDisplayTime.setText(String.format("%02d:%02d %s ", hour, minute, ampm));
            }
        };

        photo_up = (ImageView) findViewById(R.id.photo_up);

        img_upButton = (Button) findViewById(R.id.img_upButton);
        createEventButton = (Button) findViewById(R.id.button2);
        photo_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
            switch(v.getId()){
                case R.id.photo_up:
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    break;
                case R.id.button2:
                    break;
            }
       }
       @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode,resultCode,data);
           if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data !=null){
               Uri selectedImage = data.getData();
               Picasso.with(this).load(selectedImage).into(photo_up);
           }
       }
    }


