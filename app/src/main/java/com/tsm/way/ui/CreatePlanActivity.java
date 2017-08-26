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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.model.Plan;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class CreatePlanActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreatePlanActivity";
    private static final int RESULT_LOAD_IMAGE = 14543;
    ImageView photo_up;
    Button createEventButton;
    EditText planName;
    EditText planDescriptionEditText;
    HashMap<String, Integer> dateStore;
    Plan mPlan;
    Calendar mTime;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference planRef;
    DatabaseReference userPlanRef;
    FirebaseUser user;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        planRef = database.getReference("userPlans");
        userPlanRef = planRef.child(user.getUid());

        mPlan = new Plan();
        mDisplayDate = (TextView) findViewById(R.id.datepick);
        mDisplayTime = (TextView) findViewById(R.id.timepick);
        planName = (EditText) findViewById(R.id.name_editText);
        planDescriptionEditText = (EditText) findViewById(R.id.desc_edittext);
        dateStore = new HashMap<>();

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
                dateStore.put("month", month);
                dateStore.put("year", year);
                dateStore.put("day", day);
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
                //mTime = new GregorianCalendar(year,month,day);
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
                dateStore.put("hour", hour);
                dateStore.put("minute", minute);
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
        createEventButton = (Button) findViewById(R.id.button2);
        photo_up.setOnClickListener(this);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preparePlan();
            }
        });
    }

    private void preparePlan() {
        String title = planName.getText().toString();
        if (!title.equals("")) {
            mPlan.setTitle(title);
        } else {
            Toast.makeText(this, "Invalid title", Toast.LENGTH_SHORT).show();
            return;
        }
        String desc = planDescriptionEditText.getText().toString();
        if (!desc.equals("")) {
            mPlan.setDescription(desc);
        }
        mPlan.setStatus(true);
        if (dateStore.get("minute") != null && dateStore.get("day") != null) {
            Calendar timeCal = new GregorianCalendar(dateStore.get("year"), dateStore.get("month"), dateStore.get("day"), dateStore.get("hour"), dateStore.get("minute"), 0);
            mPlan.setStartTime(timeCal.getTimeInMillis());
        }
        //TODO Hard coding info, change later
        mPlan.setPlaceAddress("Dhaka");

        userPlanRef.push().setValue(mPlan);
        Toast.makeText(this, "Plan added", Toast.LENGTH_SHORT).show();
        finish();
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


