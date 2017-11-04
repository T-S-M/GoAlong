package com.tsm.way.ui.plan.fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Plan;
import com.tsm.way.models.Task;
import com.tsm.way.ui.plan.adapters.TaskViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button addTaskButtton;
    private DatabaseReference taskref;
    private FirebaseRecyclerAdapter mAdapter;


    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        addTaskButtton = view.findViewById(R.id.openUserInputDialog);
        final Plan mPlan = getArguments().getParcelable("plan");

        taskref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("tasks").child(mPlan.getDiscussionID());

        if (user.getUid().equals(mPlan.getHostUid())) {
            enableEventAdminFeatures(view);
        } else {
            addTaskButtton.setVisibility(View.GONE);
        }
        RecyclerView taskview = view.findViewById(R.id.tasks_list_view);
        taskview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));

        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(taskref, Task.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<Task, TaskViewHolder>(options) {

            @Override
            public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
                return new TaskViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(TaskViewHolder holder, int position, Task model) {
                holder.setAssigneeTextView(model.getAssignee());
                holder.setTaskNameTextView(model.getTaskName());
                holder.setDetailTextView(model.getTaskDetail());
            }
        };
        taskview.setAdapter(mAdapter);
        taskview.setClickable(false);

        return view;
    }

    private void enableEventAdminFeatures(View view) {
        addTaskButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.input_dialogue_add_task, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
                alertDialogBuilderUserInput.setView(mView);

                final EditText nameDialogEditText = mView.findViewById(R.id.task_name);
                final EditText personDialogEditText = mView.findViewById(R.id.task_person);
                final EditText descriptionDialogEditText = mView.findViewById(R.id.task_desc);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String task_name = nameDialogEditText.getText().toString();
                                String assignee = personDialogEditText.getText().toString();
                                String description = descriptionDialogEditText.getText().toString();
                                taskref.push().setValue(new Task(assignee, task_name, user.getDisplayName(), user.getUid(), description));
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
