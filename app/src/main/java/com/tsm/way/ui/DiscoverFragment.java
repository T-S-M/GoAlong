package com.tsm.way.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tsm.way.R;
import com.tsm.way.utils.CategoriesUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private GridView categoriesGridView;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        categoriesGridView = (GridView) rootView.findViewById(R.id.main_categories);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(), CategoriesUtil.getCategories());
        categoriesGridView.setAdapter(categoriesAdapter);
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlaceListActivity.class);
                intent.putExtra("type", (String) view.getTag());
                startActivity(intent);
            }
        });

        return rootView;
    }




}


