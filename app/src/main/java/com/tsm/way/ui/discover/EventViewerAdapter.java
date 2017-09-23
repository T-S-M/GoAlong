package com.tsm.way.ui.discover;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.model.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by mahsin on 9/21/17.
 */

public class EventViewerAdapter extends RecyclerView.Adapter<EventViewerAdapter.EventViewerAdapterViewHolder> {

    private final EventViewerAdapterOnclickHandler mClickHandler;
    Context mContext;
    List<Plan> fbEventList;

    public EventViewerAdapter(Context mContext, List<Plan> fbEventList, EventViewerAdapterOnclickHandler mClickHandler) {
        this.mContext = mContext;
        this.fbEventList = fbEventList;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public EventViewerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.facebook_event_card, parent, false);
        return new EventViewerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewerAdapterViewHolder holder, int position) {
        Plan model = fbEventList.get(position);
        holder.planNameTextView.setText(model.getTitle());
        Log.v("FB_IMAGE", model.getTitle());
        holder.planPlaceTextView.setText(model.getPlaceName());
        long unixTime = model.getStartTime();
        Date date = new java.util.Date(unixTime);
        String formattedTime = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(date);
        holder.planDateTime.setText(formattedTime);
        if (model.getCoverUrl() != null) {
            String url = model.getCoverUrl();
            Log.v("FB_IMAGE", url);
            Picasso.with(mContext)
                    .load(url)
                    .into(holder.coverPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return fbEventList.size();
    }


    public interface EventViewerAdapterOnclickHandler {

    }

    public class EventViewerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View viewItem;
        TextView planNameTextView;
        TextView planPlaceTextView, planDateTime;
        ImageView coverPhoto;

        public EventViewerAdapterViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;
            planNameTextView = (TextView) itemView.findViewById(R.id.plan_name_card);
            planPlaceTextView = (TextView) itemView.findViewById(R.id.plan_address_in_card);
            planDateTime = (TextView) itemView.findViewById(R.id.plan_date_time_in_card);
            coverPhoto = (ImageView) itemView.findViewById(R.id.plan_cover_image);
        }

        @Override
        public void onClick(View v) {
            //String placeID = placeBeanList.get(getAdapterPosition()).getPlaceref();
            //mClickHandler.onClick(placeID);
            //todo EventActivity
        }
    }
}
