package com.tsm.way.ui.saved;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tsm.way.ui.discover.EventViewerAdapter;
import com.tsm.way.ui.discover.FixedPlaceListAdapter;

/**
 * Created by mahsin on 10/10/17.
 */

public class SavedViewAdapter extends RecyclerView.Adapter {

    Context mContext;
    FixedPlaceListAdapter placeAdapter;
    EventViewerAdapter eventAdapter;

    public SavedViewAdapter(Context context, FixedPlaceListAdapter placeAdapter, EventViewerAdapter eventAdapter) {
        mContext = context;
        this.placeAdapter = placeAdapter;
        this.eventAdapter = eventAdapter;
    }

    public void setPlaceAdapter(FixedPlaceListAdapter placeAdapter) {
        this.placeAdapter = placeAdapter;
        notifyDataSetChanged();
    }

    public void setEventAdapter(EventViewerAdapter eventAdapter) {
        this.eventAdapter = eventAdapter;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
