package edu.osu.baskets.foods;

import android.content.Context;
import android.util.Log;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

public class strawberries extends BaseFood {

    public strawberries(Context context){
        super(context);

        AddTag("fruit");
        SetMaxStackSize(mContext.getResources().getInteger(R.integer.stack_large));
        SetCalories(mContext.getResources().getInteger(R.integer.calories_medium));
    }

    @Override
    public void onEaten(String eater) {
        super.onEaten(eater);
        Log.d("strawberries", ">> Eater: " + eater);
    }

}
