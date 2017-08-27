package com.tsm.way.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.model.PlaceBean;
import com.tsm.way.utils.UrlsUtil;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceListAdapterViewHolder> {

    private final PlaceListAdapterOnclickHandler mClickHandler;
    Context mContext;
    List<PlaceBean> placeBeanList;

    public PlaceListAdapter(Context mContext, List<PlaceBean> placeBeanList, PlaceListAdapterOnclickHandler mClickHandler) {
        this.mContext = mContext;
        this.placeBeanList = placeBeanList;
        this.mClickHandler = mClickHandler;
    }

    public void setData(List<PlaceBean> placeBeanList) {
        this.placeBeanList = placeBeanList;
        notifyDataSetChanged();
    }

    @Override
    public PlaceListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_place_in_list, parent, false);
        return new PlaceListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceListAdapterViewHolder holder, int position) {
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
        if (pb.getPhotoref() != null) {
            String imageUrl = UrlsUtil.getSinglePhotoUrlString(mContext, pb.getPhotoref(), "350", "300");
            Picasso.with(mContext)
                    .load(imageUrl)
                    .into(holder.thumbnailImage);
        }
    }

    @Override
    public int getItemCount() {
        return placeBeanList.size();
    }

    public interface PlaceListAdapterOnclickHandler {
        void onClick(String id);
    }

    public class PlaceListAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        TextView nameTextView;
        TextView openTextView;
        TextView addressTextView;
        RatingBar rating;
        ImageView thumbnailImage;

        public PlaceListAdapterViewHolder(View itemView) {
            super(itemView);
            openTextView = (TextView) itemView.findViewById(R.id.open_now_in_list);
            nameTextView = (TextView) itemView.findViewById(R.id.place_name_now_in_list);
            addressTextView = (TextView) itemView.findViewById(R.id.address_in_list);
            rating = (RatingBar) itemView.findViewById(R.id.rating_single_place_in_list);
            thumbnailImage = (ImageView) itemView.findViewById(R.id.place_image_thumb);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String placeID = placeBeanList.get(getAdapterPosition()).getPlaceref();
            mClickHandler.onClick(placeID);
        }
    }
}
