package com.tsm.way.utils;

import android.content.Context;
import android.content.Intent;

import com.tsm.way.ui.discover.activities.EventDetailActivity;
import com.tsm.way.ui.discover.adapters.EventListAdapter;

public class EventCardClickHandler implements EventListAdapter.EventListAdapterOnclickHandler {
    Context mContext;

    public EventCardClickHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onClick(String id) {
        Intent intentToStartDetail = new Intent(mContext, EventDetailActivity.class);
        intentToStartDetail.putExtra("id", id);
        mContext.startActivity(intentToStartDetail);
    }
}
