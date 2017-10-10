package com.tsm.way.ui.plan;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;
import com.tsm.way.model.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    DatabaseReference taskref;
    FirebaseListAdapter mAdapter;
    private Button mButton;


    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        mButton = view.findViewById(R.id.openUserInputDialog);
        final Plan mPlan = getArguments().getParcelable("plan");
        taskref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("tasks").child(mPlan.getDiscussionID());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.input_dialogue_add_task, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
                alertDialogBuilderUserInput.setView(mView);

                final EditText nameDialogEditText = mView.findViewById(R.id.task_name);
                final EditText personDialogEditText = mView.findViewById(R.id.task_person);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String task_name = nameDialogEditText.getText().toString();
                                String assignee = personDialogEditText.getText().toString();
                                taskref.push().setValue(new Task(assignee, task_name));
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

        ListView taskview = view.findViewById(R.id.tasks_list_view);

        FirebaseListOptions<Task> options = new FirebaseListOptions.Builder<Task>()
                .setQuery(taskref, Task.class)
                .setLayout(R.layout.list_item_task)
                .build();
        mAdapter = new FirebaseListAdapter<Task>(options) {
            @Override
            protected void populateView(View v, Task model, int position) {
                ((TextView) v.findViewById(R.id.assignee_name_tv)).setText(model.getAssignee());
                ((TextView) v.findViewById(R.id.task_name_tv)).setText(model.getTaskName());
            }
        };
        taskview.setAdapter(mAdapter);


        return view;
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
