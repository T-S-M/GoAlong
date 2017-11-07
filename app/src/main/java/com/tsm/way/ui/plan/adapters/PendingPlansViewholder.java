package com.tsm.way.ui.plan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.models.Plan;
import com.tsm.way.utils.ConstantsHelperMethodsUtil;

public class PendingPlansViewholder extends RecyclerView.ViewHolder {
    private TextView titleTextview;
    private TextView infoTextView;
    private ImageButton ignoreButton;
    private ImageButton expandbutton;
    private ImageButton acceptButton;

    public PendingPlansViewholder(View itemView) {
        super(itemView);
        titleTextview = itemView.findViewById(R.id.pending_plan_title_tv);
        infoTextView = itemView.findViewById(R.id.textView);
        ignoreButton = itemView.findViewById(R.id.ignore_button);
        acceptButton = itemView.findViewById(R.id.accept_button);
        expandbutton = itemView.findViewById(R.id.expand_button);
    }

    public void bindDataToViewHolder(final Plan model, Context context, final PendingClickHandler clickHandler) {
        titleTextview.setText(model.getTitle());
        String info = "On " + ConstantsHelperMethodsUtil.getFormattedTimeFromTimestamp(model.getStartTime()) + " in \n" + model.getPlaceName();
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
