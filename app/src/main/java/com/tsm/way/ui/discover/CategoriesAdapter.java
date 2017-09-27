package com.tsm.way.ui.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.utils.CategoriesUtil;
import com.tsm.way.utils.Category;

public class CategoriesAdapter extends BaseAdapter {

    Category[] categories;
    Context context;

    public CategoriesAdapter(Context context, Category[] categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Category category = categories[position];

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.single_category, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.category_item_icon);
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.category_item_name);

        imageView.setImageResource(category.getIconID());
        String name = category.getName();
        nameTextView.setText(name);

        convertView.setTag(CategoriesUtil.getApiplaceType(name));

        return convertView;
    }


}
