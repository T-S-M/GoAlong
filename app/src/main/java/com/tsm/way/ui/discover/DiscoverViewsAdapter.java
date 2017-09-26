package com.tsm.way.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.tsm.way.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by mahsin on 9/26/17.
 */

public class DiscoverViewsAdapter extends RecyclerView.Adapter {
    private final int FB_EVENTS = 0, RESTAURANT = 1, CATEGORY = 2;
    private List<Object> items;

    public DiscoverViewsAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Switch(viewType) {
            case RESTAURANT:
                View v = Inflater.inflate(R.layout.layout_discover_restaurants);
            case CATEGORY:
                View v = Inflater.inflate(R.layout.layout_discover_category);
            case FB_EVENTS:
                View v = Inflater.inflate(R.layout.layout_discover_fb_events);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Switch(position) {
            case 1:
                return RESTAURANT;
            case 2:
                return CATEGORY;
            case 0:
                return FB_EVENTS;
            default:
                return null;
        }
    }
}
