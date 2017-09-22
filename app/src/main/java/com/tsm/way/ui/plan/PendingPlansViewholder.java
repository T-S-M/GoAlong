package com.tsm.way.ui.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.model.Plan;

public class PendingPlansViewholder extends RecyclerView.ViewHolder {
    TextView titleTextview;
    TextView infoTextView;

    public PendingPlansViewholder(View itemView) {
        super(itemView);
        titleTextview = (TextView) itemView.findViewById(R.id.pending_plan_title_tv);
        infoTextView = (TextView) itemView.findViewById(R.id.textView);
    }

    public void bindDataToViewHolder(Plan model, Context context) {
        titleTextview.setText(model.getTitle());
        String info = "On " + model.getStartTime() + "in \n" + model.getPlaceName();
        infoTextView.setText(info);
    }
}
