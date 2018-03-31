package edu.osu.baskets;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import edu.osu.baskets.foods.IFood;

/**
 * Created by Alberto on 2/26/2018.
 */

public class FoodUtils {
    public static Map<String, FoodConstructor> Constructors = new HashMap<>();

    public static void PopulateConstructors(Context context){
        //loop over 'prefablist' to create dictionary with <mPrefab, FoodConstructor> pairs
        for (String prefab : context.getResources().getStringArray(R.array.prefablist)) {
            try {
                FoodConstructor constr = new FoodConstructor(context, prefab);
                Constructors.put(prefab, constr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static IFood Spawn(String prefab) {
        return Spawn(prefab, 1);
    }
    public static IFood Spawn(String prefab, int stackSize) {
        IFood item = Constructors.get(prefab).Make();
        item.SetStackSize(stackSize);
        return item;
    }

    // args format: [prefab, stacksize, days-to-expire]
    public static IFood SpawnFromArgs(String[] args) {
        IFood item = Spawn(args[0], Integer.parseInt(args[1]));
        item.SetDaysToExpire(Integer.parseInt(args[2]));
        return item;
    }
}
