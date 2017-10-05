package com.tsm.way.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.firebase.LinkFacebookActivity;
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
    float plans_count[] = { 4f, 3f ,6f , 2f};
    String plans_type[] ={"Interested", "Joined","Created","Pending"};
    private Description desc;

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
        CircleImageView profilePhoto = view.findViewById(R.id.user_profile_photo);
        String photoUrl;
        if (user.getPhotoUrl() != null) photoUrl = user.getPhotoUrl().toString();
        else
            photoUrl = UrlsUtil.getGravatarUrl(user.getEmail(), "wavatar");
        Picasso.with(getContext())
                .load(photoUrl)
                .into(profilePhoto);

        DatabaseReference dbref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("users").child(user.getUid());
        Map temp = new HashMap<String, String>();
        temp.put("photoUrl", photoUrl);
        temp.put("displayName", user.getDisplayName());
        dbref.updateChildren(temp);

        //Designing the bar
       /* BarChart barChart = (BarChart) view.findViewById(R.id.chart1);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setVisibleXRangeMaximum(50);
        barChart.setScrollContainer(true);
        barChart.setFitBars(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setDragDecelerationEnabled(true);
        barChart.getDescription().setText("Achievements");

        //Initializing
        ArrayList<BarEntry> barEntries= new ArrayList<>();
        barEntries.add(new BarEntry(1, 40f));
        barEntries.add(new BarEntry(2, 45f));
        barEntries.add(new BarEntry(3, 30f));
        barEntries.add(new BarEntry(4, 35f));
        barEntries.add(new BarEntry(5, 0f));

        BarDataSet bardataset = new BarDataSet(barEntries, "Cells");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        //animation & edit
        BarData data = new BarData(bardataset);
        data.setBarWidth(0.5f);
        barChart.setData(data);
        barChart.animateXY(3000, 3000);

        String[] months = new String[] {"Just Started","Jan","Feb","Mar","April","May","June"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinimum(1);
        ///barchart done
*/
        //starting pie chart
        setupPieChart(view);

        ImageButton fbButton = view.findViewById(R.id.fbButton);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LinkFacebookActivity.class);
                //intent.putExtra("click", true);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
       // MainActivity.mNavigationDrawer.setToolbar(getActivity(), toolbar, true);
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

    private void setupPieChart(View view) {
        //populating pie entries

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i< plans_count.length ; i++){
            pieEntries.add(new PieEntry(plans_count[i], plans_type[i]));
        }
        PieDataSet dataset = new PieDataSet(pieEntries, " Statistics of Plans");
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS); // JOYFUL_COLORS / VORDIPLOM_COLORS/ COLORFUL_COLORS/ LIBERTY_COLORS/ PASTEL_COLORS
        PieData pdata = new PieData(dataset);

        //get the chart
        PieChart chart = view.findViewById(R.id.chart);
        chart.setData(pdata);
        chart.animateXY(2000, 2000);
        chart.getDescription().setText("Plan Statistics");
        chart.invalidate();

    }

    public void Description(String desc) {
        this.Description("Achievements");
    }

    public class MyAxisValueFormatter implements IAxisValueFormatter{
        private String[] mValues;
        public  MyAxisValueFormatter(String[] values){
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis){
            return  mValues[(int)value];
        }
    }

}
