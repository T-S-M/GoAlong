package com.tsm.way.utils;

import com.tsm.way.R;

public class CategoriesUtil {
    public static Category[] getCategories() {
        Category[] categories = {
                new Category("Event", R.drawable.ic_restaurant_black_24dp),
                new Category("Restaurant", R.drawable.ic_restaurant_black_24dp),
                new Category("Cafe", R.drawable.ic_restaurant_black_24dp),
                new Category("Park", R.drawable.ic_restaurant_black_24dp)
        };

        return categories;
    }
}
