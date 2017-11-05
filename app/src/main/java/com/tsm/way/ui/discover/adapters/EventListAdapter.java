package com.tsm.way.ui.discover.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tsm.way.R;
import com.tsm.way.models.Plan;
import com.tsm.way.utils.EventCardClickHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListAdapterViewHolder> {

    private final EventListAdapterOnclickHandler mClickHandler;
    Context mContext;
    ArrayList<Plan> fbEventList;

    public EventListAdapter(Context mContext, ArrayList<Plan> placeBeanList, EventCardClickHandler mClickHandler) {
        this.mContext = mContext;
        this.fbEventList = placeBeanList;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public EventListAdapter.EventListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_event_in_list, parent, false);
        return new EventListAdapter.EventListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventListAdapter.EventListAdapterViewHolder holder, int position) {
        Plan mplan = fbEventList.get(position);
        holder.nameTextView.setText(mplan.getTitle());
        holder.addressTextView.setText(mplan.getPlaceAddress());
        String formattedTime = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(mplan.getStartTime());
        holder.startTimeTextView.setText(formattedTime);
        if (mplan.getCoverUrl() != null) {
            Glide.with(mContext)
                    .load(mplan.getCoverUrl())
                    .into(holder.thumbnailImage);
        }
    }

    @Override
    public int getItemCount() {
        return fbEventList.size();
    }

    public interface EventListAdapterOnclickHandler {
        void onClick(String id);
    }

    public class EventListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView startTimeTextView;
        TextView addressTextView;
        ImageView thumbnailImage;

        public EventListAdapterViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name_now_in_list);
            startTimeTextView = itemView.findViewById(R.id.start_time_in_list);
            addressTextView = itemView.findViewById(R.id.address_in_list);
            thumbnailImage = itemView.findViewById(R.id.event_image_in_list);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String placeID = fbEventList.get(getAdapterPosition()).getGooglePlaceID();
            mClickHandler.onClick(placeID);
        }
    }
}