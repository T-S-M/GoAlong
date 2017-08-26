package com.tsm.way.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tsm.way.R;

public class PlanCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView planNameTextView;
    TextView planDescriptionTextView, planDateTime;

    public PlanCardViewHolder(View itemView) {
        super(itemView);
        planNameTextView = (TextView) itemView.findViewById(R.id.plan_name_card);
        planDescriptionTextView = (TextView) itemView.findViewById(R.id.plan_desc_in_card);
        planDateTime = (TextView) itemView.findViewById(R.id.plan_date_time_in_card);
    }

    public void setPlanName(String name) {
        planNameTextView.setText(name);
    }

    public void setDescription(String desc) {
        planDescriptionTextView.setText(desc);
    }

    public void setPlanDateTime(String dateTime) {
        planDateTime.setText(dateTime);
    }

    @Override
    public void onClick(View v) {

    }
}
