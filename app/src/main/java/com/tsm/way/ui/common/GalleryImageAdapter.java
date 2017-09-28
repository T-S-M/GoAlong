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
import com.tsm.way.model.PlaceBean;
import com.tsm.way.utils.UrlsUtil;

import java.util.List;

/**
 * Created by Sakib on 9/28/2017.
 */

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.GalleryImageAdapterViewHolder> {

    String[] photos;
    Context context;
    String baseurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    String key = "AIzaSyBg-iwzAjavEUVV9hOQUr0JljZHL7XFRkQ";
    List<PlaceBean> placeBeanList;

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

       // String[] photos = photos[position];
        PlaceBean pb = placeBeanList.get(position);
        if (pb.getPhotoref() != null) {
            String imageUrl = UrlsUtil.getSinglePhotoUrlString(context, pb.getPhotoref(), "350", "300");
            Picasso.with(context)
                    .load(imageUrl)
                    .into(holder.galleryItem);
        }
        else {
            holder.no_Image.setText("Sorry, no images available for this place");
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

    public class GalleryImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView galleryItem;
        TextView no_Image;

        public GalleryImageAdapterViewHolder(View itemView) {
            super(itemView);

            TextView no_Image = (TextView) itemView.findViewById(R.id.no_images);
            no_Image.setText("Sorry, no images available for this place");

            galleryItem = (ImageView) itemView.findViewById(R.id.galleryItem);
            String imageURL = baseurl + getItem(getPosition()) + "&key=" + key;
            Picasso.with(context).load(imageURL).into(galleryItem);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
