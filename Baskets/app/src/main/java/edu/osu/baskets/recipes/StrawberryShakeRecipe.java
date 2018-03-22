package edu.osu.baskets.recipes;

import android.content.Context;

import edu.osu.baskets.R;
import edu.osu.baskets.foods.BaseFood;
import edu.osu.baskets.foods.IFood;
import edu.osu.baskets.foods.strawberries;
import edu.osu.baskets.foods.water;

/**
 * Created by Daniel on 3/21/2018.
 */

public class StrawberryShakeRecipe extends BaseRecipe {
    public StrawberryShakeRecipe(Context context){
        BaseFood strawberries, water;
        strawberries = new strawberries(context);
        strawberries.SetPrefab(context.getString(R.string.strawberries));
        water = new water(context);
        water.SetPrefab(context.getString(R.string.water));
        requiredFoods.add(strawberries);
        requiredAmounts.add(5);
        requiredFoods.add(water);
        requiredAmounts.add(2);
        title = context.getString(R.string.strawberry_shake);
        calories = 120;
    }
}
