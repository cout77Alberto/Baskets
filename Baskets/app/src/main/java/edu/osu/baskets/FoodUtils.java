package edu.osu.baskets;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import edu.osu.baskets.foods.IFoodItem;

/**
 * Created by Alberto on 2/26/2018.
 */

public class FoodUtils {
    public static Map<String, FoodConstructor> Constructors = new HashMap<>();

    public static void PopulateConstructors(Context context){
        //loop over 'prefablist' to create dictionary with <prefab, FoodConstructor> pairs
        for (String prefab : context.getResources().getStringArray(R.array.prefablist)) {
            try {
                FoodConstructor constr = new FoodConstructor(context, prefab);
                Constructors.put(prefab, constr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static IFoodItem Spawn(String prefab) {
        return Constructors.get(prefab).Make();
    }
}
