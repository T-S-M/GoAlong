package com.tsm.way.utils;

import com.tsm.way.R;

public class CategoriesUtil {
    public static Category[] getCategories() {
        Category[] categories = {
                new Category("Popular", R.drawable.ic_local_offer_black_24dp),
                new Category("Restaurant", R.drawable.ic_restaurant_black_24dp),
                new Category("Cafe", R.drawable.ic_local_cafe_black_24dp),
                new Category("Park", R.drawable.ic_local_florist_black_24dp),
                new Category("ATM", R.drawable.ic_local_atm_black_24dp),
                new Category("Movie Theatre", R.drawable.ic_tv_black_24dp),
                new Category("Museum", R.drawable.ic_account_balance_black_24dp),
                new Category("Lodging", R.drawable.ic_hotel_black_24dp),
                new Category("Hospital",R.drawable.ic_local_hospital_black_24dp)
                //new Category("Bar", R.drawable.ic_local_bar_black_24dp),
                //new Category("Spa", R.drawable.ic_spa_black_24dp),
                // new Category("Zoo", R.drawable.ic_restaurant_black_24dp)
        };

        return categories;
    }

    public static String getApiplaceType(String name) {
        if (name.equals("Movie Theatre")) {
            return "movie_theater";
        } else if (name.equals("Popular")) {
            return "points_of_interest";
        } else {
            return name.toLowerCase();
        }
    }
}
