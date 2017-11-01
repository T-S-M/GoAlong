package com.tsm.way.ui.profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.tsm.way.utils.UrlsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tsm.way.ui.common.activities.MainActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseUser user;
    float plans_count[] = {0f, 0f ,0f , 0f};
    String plans_type[] = {"Interested", "Joined","Created","Invited people"};
    TextView bio,contact,note;
    Guest appUser;
    Chart stat;
    String photoUrl;
    DatabaseReference dbref,statRef;
    LinearLayout contactLayout,statusLayout;
    PieChart chart;
    boolean isVisitor;
    ImageView fbButton;
    ImageView GoogleButton;
    private Description desc;
    private ImageView editBio;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Bundle info = getArguments();
        if (info != null) {
            isVisitor = true;
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) view.findViewById(R.id.user_profile_name)).setText(user.getDisplayName());

        note = view.findViewById(R.id.status);

        editBio =  view.findViewById(R.id.editBio);
        bio = view.findViewById(R.id.user_profile_short_bio);

        contact = view.findViewById(R.id.contact);
        contactLayout = view.findViewById(R.id.contactLayout);

        note = view.findViewById(R.id.status);
        statusLayout = view.findViewById(R.id.statusLayout);

        chart = view.findViewById(R.id.PieChart);

        CircleImageView profilePhoto = view.findViewById(R.id.user_profile_photo);
        fbButton = view.findViewById(R.id.fb_button);
        GoogleButton = view.findViewById(R.id.google_button);
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
        controlEditAccess();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        TextView friends_num = view.findViewById(R.id.friends_num);
        int friends_total = 0;
        if (friends_total == 0) {
            friends_num.setText("Total Friends: " + friends_total + "\nPlease, add/invite some friends and Enjoy!");
            friends_num.setTextSize(16f);
        } else friends_num.setText("Total Friends: " + friends_total);

        setRetainInstance(true);
        return view;
    }

    private void controlEditAccess() {
        if (!isVisitor) {
            editBio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bioEdit();
                }
            });

            // Adding / Edit the contact
            contactLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addContact();
                }
            });

            // Adding / Edit the note
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
        }

    }

    public void bioEdit(){
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

    public void  addContact(){
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
    public void addNote(){

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
    public void setDescription(String desc) {
        this.setDescription("Achievements");
    }
}
