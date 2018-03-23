package edu.osu.baskets.foods;

import android.content.Context;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

public class potatoes extends BaseFood {

    public potatoes(Context context){
        super(context);

        AddTag("starch");
        SetMaxStackSize(mContext.getResources().getInteger(R.integer.stack_medium));
        SetCalories(mContext.getResources().getInteger(R.integer.calories_medium));
    }

}
