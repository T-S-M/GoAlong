package com.tsm.way.ui.plan.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Comment;
import com.tsm.way.ui.plan.adapters.CommentViewHolder;
import com.tsm.way.utils.ConstantsHelperMethodsUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanDiscussionFragment extends Fragment {

    LinearLayoutManager mLayoutManager;
    RecyclerView messagesRecyclerView;
    RecyclerView.AdapterDataObserver observer;
    private FirebaseRecyclerAdapter mAdapter;

    public PlanDiscussionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_discussion, container, false);
        messagesRecyclerView = view.findViewById(R.id.messages);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        messagesRecyclerView.setLayoutManager(mLayoutManager);
        String id = getArguments().getString("id");

        final DatabaseReference ref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("discussion").child(id);

        FirebaseRecyclerOptions<Comment> options =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(ref, Comment.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_comment, parent, false);
                return new CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(CommentViewHolder holder, int position, Comment model) {
                holder.setName(model.getName());
                holder.setMessage(model.getMessage());
                holder.setTimestamp(ConstantsHelperMethodsUtil.getFormattedTimeFromTimestamp(model.getTimestamp()));
            }
        };
        createDataObserver();
        messagesRecyclerView.setAdapter(mAdapter);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final EditText message = view.findViewById(R.id.edittext_chatbox);
        view.findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.push().setValue(new Comment(user.getDisplayName(), message.getText().toString(), user.getUid(), System.currentTimeMillis()));
                message.setText("");
            }
        });
        return view;
    }

    private void createDataObserver() {
        observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int commentCount = mAdapter.getItemCount();
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();

                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (commentCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    messagesRecyclerView.scrollToPosition(positionStart);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.unregisterAdapterDataObserver(observer);
        mAdapter.stopListening();
    }

}
