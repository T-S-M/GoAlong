package com.tsm.way.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.tsm.way.model.PlaceBean;
import com.tsm.way.model.PlaceDetailBean;
import com.tsm.way.R;

import java.util.List;

/**
 * Created by Sakib on 8/13/2017.
 **/

public class ReviewAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceListAdapterViewHolder> {
    PlaceDetailBean.Review []reviews;
    Context context;
    List<PlaceBean> placeBeanList;
    String colors = "#005968";

    public ReviewAdapter(PlaceDetailBean.Review []reviews, Context context){
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviews.length;
    }

    @Override
    public Object getItem(int position) {
        return reviews[position];
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceListAdapter.PlaceListAdapterViewHolder holder, int position) {
        PlaceBean pb = placeBeanList.get(position);
        holder.nameTextView.setText(pb.getName());
        holder.addressTextView.setText(pb.getVicinity());
        holder.rating.setRating(pb.getRating());
        //holder.timeTextView.setText(pb.);
        if (pb.isOpen()) {
            holder.openTextView.setText(R.string.place_status_open);
        } else {
            holder.openTextView.setText(R.string.place_status_closed);
        }
    }

    @Override
    public int getItemCount(){
        return placeBeanList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView author_name;
        TextView author_text;
        RatingBar author_rating;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            ImageView icon = (ImageView) findViewById(R.id.author_icon);
            TextView author_name = (TextView) findViewById(R.id.author_name);
            TextView author_text = (TextView) findViewById(R.id.author_text);
            RatingBar author_rating = (RatingBar) findViewById(R.id.author_rating);

            author_name.setText(reviews[position].getAuthor_name());
            author_text.setText(reviews[position].getAuthor_text());
            author_rating.setRating(reviews[position].getRating());
            icon.setScaleType(ImageView.ScaleType.CENTER);
            GradientDrawable gd = (GradientDrawable) icon.getBackground().getCurrent();
            gd.setColor(Color.parseColor(colors));
        }

        @Override
        public void onClick(View v) {
        }
    }
}
