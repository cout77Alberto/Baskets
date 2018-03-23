package edu.osu.baskets.foods;

import android.content.Context;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

public class sugar extends BaseFood {

    public sugar(Context context){
        super(context);

        AddTag("sweet");
        SetMaxStackSize(mContext.getResources().getInteger(R.integer.stack_large));
        SetCalories(mContext.getResources().getInteger(R.integer.calories_large));
    }

}
