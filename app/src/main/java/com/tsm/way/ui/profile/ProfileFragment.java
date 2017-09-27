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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tsm.way.R;
import com.tsm.way.firebase.LinkFacebookActivity;

import java.util.ArrayList;

import static com.tsm.way.ui.MainActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Description desc;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        BarChart barChart = (BarChart) view.findViewById(R.id.chart1);
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

        /*ArrayList<String> labels = new ArrayList<>();
        labels.add("2016");
        labels.add("2015");
        labels.add("2014");
        labels.add("2013");
        labels.add("2012");
        */

        ArrayList<BarEntry> barEntries= new ArrayList<>();
        barEntries.add(new BarEntry(1, 40f));
        barEntries.add(new BarEntry(2, 45f));
        barEntries.add(new BarEntry(3, 30f));
        barEntries.add(new BarEntry(4, 35f));
        barEntries.add(new BarEntry(5, 0f));

        BarDataSet bardataset = new BarDataSet(barEntries, "Cells");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

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


        ImageButton fbButton = (ImageButton) view.findViewById(R.id.fbButton);
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
        return view;
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
