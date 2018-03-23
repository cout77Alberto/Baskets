package edu.osu.baskets.foods;

import android.content.Context;
import android.util.Log;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

public class gelatin extends BaseFood {

    public gelatin(Context context){
        super(context);

        AddTag("jiggly");
        SetMaxStackSize(mContext.getResources().getInteger(R.integer.stack_large));
        SetCalories(mContext.getResources().getInteger(R.integer.calories_small));
    }

}
