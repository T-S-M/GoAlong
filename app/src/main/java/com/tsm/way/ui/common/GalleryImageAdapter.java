package com.tsm.way.ui.common;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.utils.UrlsUtil;


public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.GalleryImageAdapterViewHolder> {

    String[] photos;
    Context context;

    public GalleryImageAdapter(String photos[], Context context) {
        this.photos = photos;
        this.context = context;
    }

    @Override
    public GalleryImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gallery_image_item, parent, false);
        return new GalleryImageAdapter.GalleryImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryImageAdapter.GalleryImageAdapterViewHolder holder, int position) {

        if (photos[position] != null) {
            String imageUrl = UrlsUtil.getSinglePhotoUrlString(context, photos[position], "350", "300");
            Picasso.with(context)
                    .load(imageUrl)
                    .into(holder.galleryItem);
        }
        else{
            holder.no_images.setText("Sorry, no images available for this place");
            holder.no_images.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    //@Override
    public Object getItem(int position) {
        return photos[position];
    }

    @Override
    public int getItemCount(){
        if (photos != null) {
            return photos.length;
        }
        return 0;
    }

    public class GalleryImageAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView galleryItem;
        public TextView no_images;

        public GalleryImageAdapterViewHolder(View itemView) {
            super(itemView);

            TextView no_images = (TextView) itemView.findViewById(R.id.no_images);
            no_images.setText("Sorry, no images available for this place");
            no_images.setVisibility(View.GONE);

            galleryItem = (ImageView) itemView.findViewById(R.id.galleryItem);
        }
    }
}
