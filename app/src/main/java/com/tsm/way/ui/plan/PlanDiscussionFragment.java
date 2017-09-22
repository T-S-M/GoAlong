package com.tsm.way.ui.plan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsm.way.R;
import com.tsm.way.model.Comment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanDiscussionFragment extends Fragment {

    FirebaseRecyclerAdapter mAdapter;

    public PlanDiscussionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_discussion, container, false);
        RecyclerView messages = (RecyclerView) view.findViewById(R.id.messages);
        messages.setLayoutManager(new LinearLayoutManager(getContext()));

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("discussion");

        mAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(
                Comment.class,
                R.layout.layout_comment,
                CommentViewHolder.class,
                ref) {
            @Override
            protected void populateViewHolder(CommentViewHolder holder, Comment model, int position) {
                holder.setName(model.getName());
                holder.setMessage(model.getMessage());
            }
        };

        messages.setAdapter(mAdapter);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final EditText message = (EditText) view.findViewById(R.id.edittext_chatbox);
        view.findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.push().setValue(new Comment(user.getDisplayName(), message.getText().toString(), user.getUid(), System.currentTimeMillis()));
                message.setText("");
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

}
