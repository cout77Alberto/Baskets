package edu.osu.baskets;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;

import edu.osu.baskets.foods.IFood;

/**
 * Created by Alberto on 2/28/2018.
 */

public class FoodConstructor {
    private Context mContext;
    private String prefab;
    private Constructor constr = null;

    private String TAG = "FoodConstructor";

    public FoodConstructor(Context context, String pref) {
        mContext = context;
        prefab = pref;

        Class foodClass = null;
        try {
            foodClass = Class.forName("edu.osu.baskets.foods." + prefab);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (foodClass != null) {
            try {
                constr = foodClass.getConstructor(Context.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (constr == null) { Log.d(TAG, ">> WARNING: FoodConstructor for '" + prefab + "' has null value for field 'constr'."); }
    }

    public IFood Make() {
        IFood item = null;
        try {
            item = (IFood) constr.newInstance(mContext);
            item.SetPrefab(prefab);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (item == null) { Log.d(TAG, ">> WARNING: Created null IFood."); }
        return  item;
    }
}
