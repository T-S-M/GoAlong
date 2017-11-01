package com.tsm.way.ui.plan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tsm.way.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView assignee, taskName, taskDetail;

    public TaskViewHolder(View itemView) {
        super(itemView);
        assignee = itemView.findViewById(R.id.assignee_name_tv);
        taskName = itemView.findViewById(R.id.task_name_tv);
        taskDetail = itemView.findViewById(R.id.task_detail);
    }

    public void setTaskNameTextView(String name) {
        taskName.setText(name);
    }

    public void setAssigneeTextView(String name) {
        assignee.setText(name);
    }

    public void setDetailTextView(String detailText) {
        taskDetail.setText(detailText);
    }
}
