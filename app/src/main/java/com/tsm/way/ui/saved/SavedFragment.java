package com.tsm.way.ui.saved;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tsm.way.R;
import com.tsm.way.model.Item;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tsm.way.ui.MainActivity.drawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {
    Button buttonSave;
    EditText edit;
    ListView listview;
    ArrayAdapter<Item> adapter;
    Item item = new Item("");

    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_saved_places, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        edit = (EditText) rootView.findViewById(R.id.edit);
        buttonSave = (Button) rootView.findViewById(R.id.butt);
        listview = (ListView) rootView.findViewById(R.id.item_list);

        List<Item> list = Item.listAll(Item.class);

        adapter = new ArrayAdapter<Item>(getContext(), android.R.layout.simple_list_item_1, list);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = new Item(edit.getText().toString());
                item.save();

                Toast.makeText(getApplicationContext(), "" + item, Toast.LENGTH_SHORT).show();
            }
        });



        /*FloatingActionButton fb = (FloatingActionButton) rootView.findViewById(R.id.fb_test);
        //fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LinkFacebookActivity.class));
            }
        });*/
        return rootView;
    }
    
}
