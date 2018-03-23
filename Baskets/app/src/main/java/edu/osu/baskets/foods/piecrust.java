package edu.osu.baskets.foods;

import android.content.Context;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

public class piecrust extends BaseFood {

    public piecrust(Context context){
        super(context);

        AddTag("pastry");
        SetMaxStackSize(mContext.getResources().getInteger(R.integer.stack_small));
        SetCalories(mContext.getResources().getInteger(R.integer.calories_medium));
    }

}
