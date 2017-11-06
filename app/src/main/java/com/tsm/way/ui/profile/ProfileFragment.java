package com.tsm.way.ui.profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsm.way.R;
import com.tsm.way.firebase.FacebookAccountHelperActivity;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Chart;
import com.tsm.way.models.Guest;
import com.tsm.way.ui.common.activities.AuthActivity;
import com.tsm.way.utils.CommonUtils;
import com.tsm.way.utils.UrlsUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tsm.way.ui.common.activities.MainActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
public class ProfileFragment extends Fragment {

    private final float[] plans_count = {0f, 0f, 0f, 0f};
    private final String[] plans_type = {"Interested", "Joined", "Created", "Invited people"};
    private final DatabaseReference rootRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference();
    private FirebaseUser user;
    private TextView bio;
    private TextView contact;
    private TextView note;
    private Guest appUser;
    private Chart stat;
    private String photoUrl;
    private String guestID;
    private DatabaseReference dbref;
    private DatabaseReference statRef;
    private LinearLayout contactLayout;
    private LinearLayout statusLayout;
    private PieChart chart;
    private boolean isVisitor;
    private ImageView fbButton;
    private ImageView fav_button;
    private ImageView GoogleButton;
    private View view;
    private CircleImageView profilePhoto;
    private Toolbar toolbar;
    private long totalPopularity;
    private Description desc;
    private ImageView editBio;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle info = getArguments();
        if (info != null) {
            isVisitor = true;
            guestID = info.getString("id");
        }

        note = view.findViewById(R.id.status);

        editBio =  view.findViewById(R.id.editBio);
        bio = view.findViewById(R.id.user_profile_short_bio);

        contact = view.findViewById(R.id.contact);
        contactLayout = view.findViewById(R.id.contactLayout);

        note = view.findViewById(R.id.status);
        statusLayout = view.findViewById(R.id.statusLayout);

        chart = view.findViewById(R.id.PieChart);

        profilePhoto = view.findViewById(R.id.user_profile_photo);
        fbButton = view.findViewById(R.id.fb_button);
        GoogleButton = view.findViewById(R.id.google_button);
        fav_button = view.findViewById(R.id.fav_button);
        TextView popularity_cnt = view.findViewById(R.id.popularity);
        controlEditAccess();

        setRetainInstance(true);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void controlEditAccess() {
        if (!isVisitor) {
            editBio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bioEdit();
                }
            });
            contactLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addContact();
                }
            });
            statusLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNote();
                }
            });

            fbButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), FacebookAccountHelperActivity.class);
                    //intent.putExtra("click", true);
                    startActivity(intent);
                }
            });

            GoogleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), AuthActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            fav_button.setVisibility(View.INVISIBLE);
            loadDataForCurrentUser();
        } else {
            fbButton.setVisibility(View.INVISIBLE);
            GoogleButton.setVisibility(View.INVISIBLE);
            editBio.setVisibility(View.INVISIBLE);
            loadDataForGuestUser();
        }

    }

    private void loadDataForGuestUser() {
        DatabaseReference guestRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("users").child(guestID);
        guestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Guest user = dataSnapshot.getValue(Guest.class);
                ((TextView) view.findViewById(R.id.user_profile_name)).setText(user.getDisplayName());
                if (user.getPhotoUrl() != null) photoUrl = user.getPhotoUrl();
                else
                    photoUrl = UrlsUtil.getGravatarUrl(user.getEmail(), "wavatar");
                Glide.with(getContext())
                        .load(photoUrl)
                        .into(profilePhoto);

                dbref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("users").child(user.getUid());
                getUserData();

                statRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("stats").child(user.getUid());
                getStatData();

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

                TextView popularityCount = view.findViewById(R.id.popularity);
                //totalPopularity ;
                if (totalPopularity != 0) {
                    popularityCount.setText("Popularity: " + totalPopularity+" Person(s) liked your profile! ");
                    popularityCount.setTextSize(16f);
                } else  popularityCount.setText("Popularity: " + totalPopularity + "\nPlease, add/invite some friends and Enjoy!");
                enableFavouriteButton(rootRef.child("popularity").child(user.getUid()), popularityCount);
                contactLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtils.dialPhoneNumber(user.getContact(), getContext());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void enableFavouriteButton(final DatabaseReference popRef, final TextView popularityCount) {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        popRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalPopularity = dataSnapshot.getChildrenCount();
                updateOrReadLikeValue(true, popRef, popularityCount, uid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrReadLikeValue(false, popRef, popularityCount, uid);
                //fav_button.setImageResource(R.drawable.ic_love);
            }
        });

    }

    private void updateOrReadLikeValue(final boolean readOnly, final DatabaseReference popRef, final TextView popularityCount, final String uid) {
        popRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (!readOnly) {
                        popRef.child(uid).removeValue();
                        fav_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        totalPopularity++;
                        popularityCount.setText("Popularity: " + String.valueOf(totalPopularity) +" Person(s) liked your profile! ");
                    } else
                        fav_button.setImageResource(R.drawable.ic_love);
                } else {
                    if (!readOnly) {
                        popRef.child(uid).setValue(true);
                        fav_button.setImageResource(R.drawable.ic_love);
                        totalPopularity--;
                        popularityCount.setText("Popularity: " + String.valueOf(totalPopularity) +" Person(s) liked your profile! ");
                    } else fav_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadDataForCurrentUser() {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) view.findViewById(R.id.user_profile_name)).setText(user.getDisplayName());
        if (user.getPhotoUrl() != null) photoUrl = user.getPhotoUrl().toString();
        else
            photoUrl = UrlsUtil.getGravatarUrl(user.getEmail(), "wavatar");
        Glide.with(getContext())
                .load(photoUrl)
                .into(profilePhoto);

        dbref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("users").child(user.getUid());
        getUserData();

        statRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("stats").child(user.getUid());
        getStatData();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        //totalPopularity ;
        TextView popularityCount = view.findViewById(R.id.popularity);
        if (totalPopularity != 0) {
            popularityCount.setText("Popularity: " + totalPopularity+" Person(s) liked your profile! ");
            popularityCount.setTextSize(16f);
        } else  popularityCount.setText("Popularity: " + totalPopularity + "\nPlease, add/invite some friends and Enjoy!");
        enableFavouriteButton(rootRef.child("popularity").child(user.getUid()), popularityCount);
        //greetings
        showGreetingMsg();
    }

    private void bioEdit() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.profilebioedit, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final EditText EditBioET = mView.findViewById(R.id.profile_bio_edit);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String profileBio = EditBioET.getText().toString();
                        bio.setText(profileBio);
                        Map temp = new HashMap<String, String>();
                        temp.put("profileBio", profileBio);
                        dbref.updateChildren(temp);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showGreetingMsg() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(sdf.format(new Date()));

        TextView greetings = view.findViewById(R.id.greetings);
        Calendar c = Calendar.getInstance();
        //int date = c.get(Calendar.DATE);
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int am_pm = c.get(Calendar.AM_PM);
        String ampm;
        if(am_pm==1){
             ampm ="PM";
        }else ampm = "AM";

        if(timeOfDay >= 0 && timeOfDay < 4){
            Snackbar.make(view, "Don't stay up too late, "+user.getDisplayName()+"?\nYou'll be late for morning work.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if(timeOfDay==0)
            greetings.setText("You should sleep now, "+user.getDisplayName()+"??"+"\nGood Morning!!!\nHave a good Day."+"\nCurrent date: "+sdf.format(new Date())+"\nTime: "+(timeOfDay+12)+":"+min+" "+ampm);
            else greetings.setText("You should sleep now, "+user.getDisplayName()+"??"+"\nGood Morning!!!\nHave a good Day."+"\nCurrent date: "+sdf.format(new Date())+"\nTime: "+(timeOfDay+12)+":"+min+" "+ampm);
        }
        if(timeOfDay >= 4 && timeOfDay < 12){
            Snackbar.make(view, "Good Morning, "+user.getDisplayName()+"!!"+" Have a good day!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            greetings.setText("How are you felling, "+user.getDisplayName()+"??"+"\nGood Morning!!!\nHave a good Day."+"\nCurrent date: "+sdf.format(new Date())+"\nTime: "+timeOfDay+":"+min+" "+ampm);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            Snackbar.make(view, "Good Afternoon, "+user.getDisplayName()+"!!"+" Don't Forget to have Lunch.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            greetings.setText("How are you felling, "+user.getDisplayName()+"??"+"\nGood Afternoon!!!"+"\nCurrent date: "+sdf.format(new Date())+"\nTime: "+(timeOfDay-12)+":"+min+" "+ampm);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            Snackbar.make(view, "Good Evening, "+user.getDisplayName()+"!!"+" Tired?? have some Snacks! :P", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            greetings.setText("How are you felling, "+user.getDisplayName()+"??"+"\nGood Evening!!!"+"\nCurrent date: "+sdf.format(new Date())+"\nTime: "+(timeOfDay-12)+":"+min+" "+ampm);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            if(timeOfDay>=21 && timeOfDay<=23)
            Snackbar.make(view, "Had Dinner, "+user.getDisplayName()+"?? Eat well Sleep well.\n               Good Night!   ...zzZZZzZZ....", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            else if(timeOfDay>23)  Snackbar.make(view, "Good Night! "+user.getDisplayName()+" :)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            greetings.setText("Feeling Tired,"+user.getDisplayName()+"?"+" Have dinner & Get Some Sleep."+"\nGood Night!!!"+"\nCurrent date: "+sdf.format(new Date())+"\nTime: "+(timeOfDay-12)+":"+min+" "+ampm);
        }
    }

    private void addContact() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.editcontact, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final EditText EditContact = mView.findViewById(R.id.editContact);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String Contact = EditContact.getText().toString();
                        contact.setText(Contact);
                        Map temp1 = new HashMap<String, String>();
                        temp1.put("contact",Contact);
                        dbref.updateChildren(temp1);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void addNote() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.addnote, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final EditText EditNote = mView.findViewById(R.id.editNote);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String Note = EditNote.getText().toString();
                        note.setText(Note);
                        Map temp2 = new HashMap<String, String>();
                        temp2.put("note",Note);
                        dbref.updateChildren(temp2);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    private void getUserData() {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appUser = dataSnapshot.getValue(Guest.class);
                if (appUser == null) {
                    updateUserData(appUser);
                } else {
                    if (appUser.getProfileBio() != null) bio.setText(appUser.getProfileBio());
                    if(appUser.getContact()!=null) contact.setText(appUser.getContact());
                    if(appUser.getNote() !=null) note.setText(appUser.getNote());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getStatData() {
        statRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stat = dataSnapshot.getValue(Chart.class);
                if (stat == null) {
                    ((TextView) getView().findViewById(R.id.no_data)).setText("No data Available!! Add Some Plans First.");
                } else {
                    setupPieChart(stat);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUserData(Guest appUser) {
        Guest guest = new Guest();
        guest.setUid(user.getUid());
        guest.setDisplayName(user.getDisplayName());
        guest.setEmail(user.getEmail());
        guest.setPhotoUrl(photoUrl);
        dbref.setValue(guest);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getDisplayName())
                .setPhotoUri(Uri.parse(photoUrl))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "User fragment_profile updated.");
                        }
                    }
                });
    }

    private void setupPieChart(Chart stat) {
        //populating pie entries
        plans_count[0] = stat.getAcceptedCount();
        plans_count[1] = stat.getAcceptedCount()+stat.getCreatedCount();
        plans_count[2] = stat.getCreatedCount();
        plans_count[3] = stat.getInvitedCount();

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i< plans_count.length ; i++){
            pieEntries.add(new PieEntry(plans_count[i], plans_type[i]));
        }
        PieDataSet dataset = new PieDataSet(pieEntries, " Statistics of Plans");
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS); // JOYFUL_COLORS / VORDIPLOM_COLORS/ COLORFUL_COLORS/ LIBERTY_COLORS/ PASTEL_COLORS
        PieData pdata = new PieData(dataset);

        //get the chart

        dataset.setValueFormatter(new MyValueFormatter());
        dataset.setValueTextSize(11f);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        chart.getLegend().setEnabled(true);
        int colorBlack = Color.parseColor("#000000");
        chart.setEntryLabelColor(colorBlack);
        chart.setData(pdata);
        //chart.animateXY(2000, 2000);
        chart.animateY(2500, Easing.EasingOption.EaseOutBounce);
        chart.getDescription().setText("Plan Statistics");
        chart.invalidate();
    }

}
