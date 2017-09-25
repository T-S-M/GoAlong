package com.tsm.way.ui.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.model.Plan;

public class PendingPlansViewholder extends RecyclerView.ViewHolder {
    TextView titleTextview;
    TextView infoTextView;
    ImageButton ignoreButton;
    ImageButton expandbutton;
    ImageButton acceptButton;

    public PendingPlansViewholder(View itemView) {
        super(itemView);
        titleTextview = (TextView) itemView.findViewById(R.id.pending_plan_title_tv);
        infoTextView = (TextView) itemView.findViewById(R.id.textView);
        ignoreButton = (ImageButton) itemView.findViewById(R.id.ignore_button);
        acceptButton = (ImageButton) itemView.findViewById(R.id.accept_button);
        expandbutton = (ImageButton) itemView.findViewById(R.id.expand_button);
    }

    public void bindDataToViewHolder(final Plan model, Context context, final PendingClickHandler clickHandler) {
        titleTextview.setText(model.getTitle());
        String info = "On " + model.getStartTime() + "in \n" + model.getPlaceName();
        infoTextView.setText(info);
        ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.onClick(v.getId(), model.getDiscussionID());
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.onClick(v.getId(), model.getDiscussionID());
            }
        });

        expandbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.onClick(v.getId(), model.getDiscussionID());
            }
        });
    }

    public interface PendingClickHandler {
        void onClick(int viewId, String planID);
    }
}
