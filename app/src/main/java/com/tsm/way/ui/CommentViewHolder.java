package com.tsm.way.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tsm.way.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNameField;
    private final TextView mMessageField;

    public CommentViewHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.text_message_name);
        mMessageField = (TextView) itemView.findViewById(R.id.text_message_body);
    }

    public void setName(String name) {
        mNameField.setText(name);
    }

    public void setMessage(String message) {
        mMessageField.setText(message);
    }
}
