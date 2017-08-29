package com.tsm.way.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tsm.way.R;
import com.tsm.way.model.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlanCardViewHolder extends RecyclerView.ViewHolder {

    View viewItem;
    TextView planNameTextView;
    TextView planDescriptionTextView, planDateTime;

    public PlanCardViewHolder(View itemView) {
        super(itemView);
        viewItem = itemView;
        planNameTextView = (TextView) itemView.findViewById(R.id.plan_name_card);
        planDescriptionTextView = (TextView) itemView.findViewById(R.id.plan_desc_in_card);
        planDateTime = (TextView) itemView.findViewById(R.id.plan_date_time_in_card);
    }

    public void bindDataToViewHolder(final Plan model, final Context context) {
        planNameTextView.setText(model.getTitle());
        planDescriptionTextView.setText(model.getDescription());
        long unixTime = model.getStartTime();
        Date date = new java.util.Date(unixTime);
        String formattedTime = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(date);
        planDateTime.setText(formattedTime);
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
