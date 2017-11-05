package com.tsm.way.ui.discover.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.ui.discover.activities.EventListActivity;
import com.tsm.way.ui.discover.activities.PlaceListActivity;
import com.tsm.way.utils.CategoriesUtil;

public class DiscoverViewsAdapter extends RecyclerView.Adapter {
    private final int FB_EVENTS = 0, RESTAURANT = 1, CATEGORY = 2;

    Context mContext;
    FixedPlaceListAdapter placeAdapter;
    EventViewerAdapter eventAdapter;

    public DiscoverViewsAdapter(Context context, FixedPlaceListAdapter placeAdapter, EventViewerAdapter eventAdapter) {
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case RESTAURANT:
                View v0 = inflater.inflate(R.layout.layout_discover_restaurants, parent, false);
                return new ViewHolderRV(v0);
            case CATEGORY:
                View v1 = inflater.inflate(R.layout.layout_discover_category, parent, false);
                return new CategoryViewHolder(v1);
            case FB_EVENTS:
                View v2 = inflater.inflate(R.layout.layout_discover_fb_events, parent, false);
                return new ViewHolderfbEvents(v2);
            default:
                View vy = inflater.inflate(R.layout.layout_discover_category, parent, false);
                return new CategoryViewHolder(vy);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderfbEvents) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            ((ViewHolderfbEvents) holder).events_recyclerview.setLayoutManager(layoutManager);
            if (eventAdapter != null) {
                ((ViewHolderfbEvents) holder).events_recyclerview.setAdapter(eventAdapter);
                ((ViewHolderfbEvents) holder).events_recyclerview.setVisibility(View.VISIBLE);
            }

            ((ViewHolderfbEvents) holder).more1.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           Intent intent = new Intent(mContext, EventListActivity.class);
                                                                           intent.putExtra("Events", "Events");
                                                                           mContext.startActivity(intent);
                                                                       }
                                                                   }
            );
        } else if (holder instanceof ViewHolderRV) {
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            ((ViewHolderRV) holder).resturants_recyclerview.setLayoutManager(layoutManager1);
            if (placeAdapter != null) {
                ((ViewHolderRV) holder).resturants_recyclerview.setAdapter(placeAdapter);
                ((ViewHolderRV) holder).resturants_recyclerview.setVisibility(View.VISIBLE);
            }


            ((ViewHolderRV) holder).more2.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     Intent intent = new Intent(mContext, PlaceListActivity.class);
                                                                     intent.putExtra("Resturants", "Resturants");
                                                                     mContext.startActivity(intent);
                                                                 }
                                                             }
            );
        } else {
            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(mContext, CategoriesUtil.getCategories());
            ((CategoryViewHolder) holder).categoriesGridView.setAdapter(categoriesAdapter);
            ((CategoryViewHolder) holder).categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, PlaceListActivity.class);
                    intent.putExtra("type", (String) view.getTag());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return FB_EVENTS;
            case 1:
                return RESTAURANT;
            case 2:
                return CATEGORY;
            default:
                return 0;
        }
    }

    class ViewHolderfbEvents extends RecyclerView.ViewHolder {

        public TextView more1;
        public RecyclerView events_recyclerview;

        public ViewHolderfbEvents(View itemView) {
            super(itemView);
            more1 = itemView.findViewById(R.id.more1);
            events_recyclerview = itemView.findViewById(R.id.events_recyclerview);
            events_recyclerview.setHasFixedSize(true);
        }
    }

    class ViewHolderRV extends RecyclerView.ViewHolder {


        public TextView more2;
        public RecyclerView resturants_recyclerview;

        public ViewHolderRV(View itemView) {
            super(itemView);
            more2 = itemView.findViewById(R.id.more2);
            resturants_recyclerview = itemView.findViewById(R.id.resturants_recyclerview_r);
            resturants_recyclerview.setHasFixedSize(true);
        }

    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        public GridView categoriesGridView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoriesGridView = itemView.findViewById(R.id.main_categories);
        }
    }
}
