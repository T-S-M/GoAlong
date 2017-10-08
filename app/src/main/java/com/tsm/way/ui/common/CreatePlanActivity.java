package com.tsm.way.ui.common;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;
import com.tsm.way.utils.ConstantsUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CreatePlanActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreatePlanActivity";
    private static final int RESULT_LOAD_IMAGE = 14543;
    int PLACE_PICKER_REQUEST = 9321;
    ImageView photo_up;
    Button createEventButton;
    EditText planName;
    EditText planDescriptionEditText;
    HashMap<String, Integer> dateStore;
    Plan mPlan;
    Uri selectedImage;
    FirebaseDatabase database = FirebaseDBHelper.getFirebaseDatabaseInstance();
    DatabaseReference planAttendeeRef;
    DatabaseReference planRef;
    DatabaseReference userPlanRef;
    FirebaseUser user;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private TextView whereTextView;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private TextView plantypeTextview;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Toolbar toolbar;
    private ProgressBar createPlanProgressBar;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        createPlanProgressBar = findViewById(R.id.create_plan_progress);
        toolbar = findViewById(R.id.toolbar_create_plan);
        toolbar.setTitle(R.string.new_plan);
        toolbar.setSubtitle(R.string.add_new_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plantypeTextview = findViewById(R.id.plan_type_textview);
        handleIntentExtras(getIntent());
        user = FirebaseAuth.getInstance().getCurrentUser();
        planAttendeeRef = database.getReference("planAttendee");
        planRef = database.getReference("plans");
        userPlanRef = database.getReference("userPlans").child(user.getUid());

        mPlan = new Plan();
        mDisplayDate = findViewById(R.id.datepick);
        mDisplayTime = findViewById(R.id.timepick);
        planName = findViewById(R.id.name_editText);
        planDescriptionEditText = findViewById(R.id.desc_edittext);
        dateStore = new HashMap<>();
        whereTextView = findViewById(R.id.where_textview);
        whereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(CreatePlanActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

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

                //Log.d(TAG, "onTimeSet: hour:minute " + hour + ":" + minute + ":" + ampm);
                //String time = hour + ":" + minute + ":" + ampm;
                mDisplayTime.setText(String.format("%02d:%02d %s ", hour, minute, ampm));
            }
        };

        photo_up = findViewById(R.id.photo_up);
        createEventButton = findViewById(R.id.button2);
        photo_up.setOnClickListener(this);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlanProgressBar.setVisibility(View.VISIBLE);
                if (selectedImage == null) {
                    preparePlan();
                } else {
                    uploadPhotoThenPrepaparePlan();
                }
            }
        });
    }

    private void handleIntentExtras(Intent intent) {
        if (intent.hasExtra("fb_event")) {

        } else if (intent.hasExtra("place_info")) {

        } else {
            plantypeTextview.setText(R.string.plan_type_personal);
        }
    }

    private void uploadPhotoThenPrepaparePlan() {
        Toast.makeText(this, "Uploading photo", Toast.LENGTH_SHORT).show();
        StorageReference coverPhotoRef = storageRef.child("planCoverPhoto/" + selectedImage.getLastPathSegment());
        uploadTask = coverPhotoRef.putFile(selectedImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CreatePlanActivity.this, "Uploading Failed :(", Toast.LENGTH_SHORT).show();
                preparePlan();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mPlan.setCoverUrl(downloadUrl.toString());
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
        mPlan.setHostName(user.getDisplayName());

        String pushKey = userPlanRef.push().getKey();

        mPlan.setDiscussionID(pushKey);

        userPlanRef.child(pushKey).setValue(mPlan.getStartTime());

        planRef.child(pushKey).setValue(mPlan);

        Map tempMap = new HashMap<String, Boolean>();
        tempMap.put(user.getUid(), true);
        planAttendeeRef.child(pushKey).updateChildren(tempMap);
        createPlanProgressBar.setVisibility(View.GONE);
        FirebaseMessaging.getInstance().subscribeToTopic(pushKey);
        //userPlanRef.push().setValue(mPlan);
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
           if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data !=null){
               selectedImage = data.getData();
               photo_up.setPadding(0, 0, 0, 0);
               photo_up.setScaleType(ImageView.ScaleType.FIT_XY);
               Picasso.with(this).load(selectedImage).into(photo_up);
           } else if (requestCode == PLACE_PICKER_REQUEST) {
               if (resultCode == RESULT_OK) {
                   Place place = PlacePicker.getPlace(this, data);
                   assignPlaceInfoForPlan(place);
               }
           }
       }

    private void assignPlaceInfoForPlan(Place place) {
        String placeName = place.getName().toString();
        mPlan.setPlaceAddress(place.getAddress().toString());
        LatLng latLng = place.getLatLng();
        mPlan.setPlaceLat(latLng.latitude);
        mPlan.setPlaceLong(latLng.longitude);
        mPlan.setPlaceName(placeName);
        whereTextView.setText(placeName);
        mPlan.setEventType(ConstantsUtil.PLAN_TYPE_PRIVATE_EVENT);
        mPlan.setGooglePlaceID(place.getId());
    }
}