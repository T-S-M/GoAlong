package com.tsm.way.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.model.PlaceDetailBean;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    PlaceDetailBean.Review reviews[];
    Context context;
    String colors = "#005968";

    public ReviewAdapter(PlaceDetailBean.Review reviews[], Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        PlaceDetailBean.Review review = reviews[position];

        holder.author_name.setText(review.getAuthor_name());
        holder.author_text.setText(review.getAuthor_text());
        holder.author_rating.setRating(review.getRating());
        holder.icon.setScaleType(ImageView.ScaleType.CENTER);
        GradientDrawable gd = (GradientDrawable) holder.icon.getBackground().getCurrent();
        gd.setColor(Color.parseColor(colors));
    }

    @Override
    public int getItemCount(){
        if (reviews != null) {
            return reviews.length;
        }
        return 0;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView author_name;
        TextView author_text;
        RatingBar author_rating;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.author_icon);
            author_name = (TextView) itemView.findViewById(R.id.author_name);
            author_text = (TextView) itemView.findViewById(R.id.author_text);
            author_rating = (RatingBar) itemView.findViewById(R.id.author_rating);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
