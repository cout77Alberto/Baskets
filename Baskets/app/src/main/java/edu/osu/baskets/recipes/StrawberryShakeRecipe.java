package edu.osu.baskets.recipes;

import android.content.Context;

import edu.osu.baskets.FoodUtils;
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
        url = "https://www.pinterest.com/search/pins/?q=strawberry%20shake%20recipe";
        BaseFood strawberries, water;
        strawberries = (BaseFood) FoodUtils.Spawn("strawberries");
        water = (BaseFood) FoodUtils.Spawn("water");
        requiredFoods.add(strawberries);
        requiredAmounts.add(5);
        requiredFoods.add(water);
        requiredAmounts.add(2);
        title = context.getString(R.string.strawberry_shake);
        calories = 120;
    }
}
