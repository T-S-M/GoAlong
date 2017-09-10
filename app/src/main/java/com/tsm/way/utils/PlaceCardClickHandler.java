package com.tsm.way.utils;

import android.content.Context;
import android.content.Intent;

import com.tsm.way.ui.FixedPlaceListAdapter;
import com.tsm.way.ui.PlaceDetailActivity;
import com.tsm.way.ui.PlaceListAdapter;

public class PlaceCardClickHandler implements FixedPlaceListAdapter.FixedPlaceListAdapterOnclickHandler,
        PlaceListAdapter.PlaceListAdapterOnclickHandler {
    Context mContext;

    public PlaceCardClickHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onClick(String id) {
        Intent intentToStartDetail = new Intent(mContext, PlaceDetailActivity.class);
        intentToStartDetail.putExtra("id", id);
        mContext.startActivity(intentToStartDetail);
    }
}
