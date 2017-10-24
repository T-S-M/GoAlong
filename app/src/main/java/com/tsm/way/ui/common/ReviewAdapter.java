package com.tsm.way.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.models.PlaceDetailBean;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    PlaceDetailBean.Review reviews[];
    Context context;

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
        // GradientDrawable gd = (GradientDrawable) holder.icon.getBackground().getCurrent();
        //gd.setColor(Color.parseColor(colors));
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

            icon = itemView.findViewById(R.id.author_icon);
            author_name = itemView.findViewById(R.id.author_name);
            author_text = itemView.findViewById(R.id.author_text);
            author_rating = itemView.findViewById(R.id.author_rating);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
