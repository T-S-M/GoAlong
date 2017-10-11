package com.tsm.way.ui.profile;


import android.content.DialogInterface;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.firebase.LinkFacebookActivity;
import com.tsm.way.model.Chart;
import com.tsm.way.model.Guest;
import com.tsm.way.ui.common.AuthActivity;
import com.tsm.way.utils.UrlsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tsm.way.ui.MainActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseUser user;
    float plans_count[] = {0f, 0f ,0f , 0f};
    String plans_type[] = {"Interested", "Joined","Created","Invited people"};
    TextView bio,contact,status;
    Guest appUser;
    Chart stat;
    String photoUrl;
    DatabaseReference dbref,stref;
    private Description desc;
    private ImageView editBio;
    LinearLayout contactLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) view.findViewById(R.id.user_profile_name)).setText(user.getDisplayName());

        status = view.findViewById(R.id.status);

        editBio =  view.findViewById(R.id.editBio);
        bio = view.findViewById(R.id.user_profile_short_bio);

        contact = view.findViewById(R.id.contact);
        contactLayout = view.findViewById(R.id.contactLayout);

        CircleImageView profilePhoto = view.findViewById(R.id.user_profile_photo);

        if (user.getPhotoUrl() != null) photoUrl = user.getPhotoUrl().toString();
        else
            photoUrl = UrlsUtil.getGravatarUrl(user.getEmail(), "wavatar");
        Picasso.with(getContext())
                .load(photoUrl)
                .into(profilePhoto);

        dbref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("users").child(user.getUid());
        getUserData();

        stref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("stats").child(user.getUid());
        getStatData();

        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.editcontact, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
                alertDialogBuilderUserInput.setView(mView);

                final EditText Editcontact = mView.findViewById(R.id.editContact);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String Contact = Editcontact.getText().toString();
                                contact.setText(Contact);
                                Map temp = new HashMap<String, String>();
                                temp.put("profileBio",contact);
                                //dbref.updateChildren(temp);
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
        });

        ImageView fbButton = view.findViewById(R.id.fb_button);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LinkFacebookActivity.class);
                //intent.putExtra("click", true);
                startActivity(intent);
            }
        });

        ImageView GoogleButton = view.findViewById(R.id.google_button);
        GoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), AuthActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        TextView friends_num = view.findViewById(R.id.friends_num);
        int friends_total = 0;
        if(friends_total==0) {
            friends_num.setText("Total Friends: " + friends_total + "\nPlease, add/invite some friends and Enjoy!");
            friends_num.setTextSize(16f);
        }
        else friends_num.setText("Total Friends: "+ friends_total);

        return view;
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getStatData() {
        stref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stat = dataSnapshot.getValue(Chart.class);
                if (stat == null) {
                    ((TextView) getView().findViewById(R.id.no_data)).setText("No data Available!! Add Some Plans First.");
                } else {
                    setupPieChart(getView(),stat);
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
                            //Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    private void setupPieChart(View view, Chart stat) {
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
        PieChart chart = view.findViewById(R.id.chart);
        dataset.setValueFormatter(new MyValueFormatter());
        chart.setData(pdata);
        chart.animateXY(2000, 2000);
        chart.getDescription().setText("Plan Statistics");
        chart.invalidate();

    }
    public void Description(String desc) {
        this.Description("Achievements");
    }
}
