package edu.osu.baskets.foods;

import android.content.Context;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 3/1/2018.
 */

//NOTE: does this extension class add enough NEW functionality to FoodItem class??
public class water extends BaseFood {

    public water(Context context){
        super(context);

        AddTag("fluid");
        SetCalories(mContext.getResources().getInteger(R.integer.calories_medium));
        SetDaysToExpire(mContext.getResources().getInteger(R.integer.expiration_days_long));
    }

}
