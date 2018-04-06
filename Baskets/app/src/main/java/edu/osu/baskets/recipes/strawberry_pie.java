package edu.osu.baskets.recipes;

import android.content.Context;

import edu.osu.baskets.FoodUtils;
import edu.osu.baskets.R;
import edu.osu.baskets.foods.BaseFood;

/**
 * Created by Daniel on 3/21/2018.
 */

public class strawberry_pie extends BaseRecipe {
    public strawberry_pie(Context context){
        url = "https://www.pinterest.com/search/pins/?q=strawberry%20pie%20recipe";
        BaseFood strawberries, water, piecrust;
        strawberries = (BaseFood) FoodUtils.Spawn("strawberries",8);
        water = (BaseFood) FoodUtils.Spawn("water",1);
        piecrust = (BaseFood) FoodUtils.Spawn("piecrust",1);
        requiredFoods.add(strawberries);
        requiredAmounts.add(8);
        requiredFoods.add(water);
        requiredAmounts.add(1);
        requiredFoods.add(piecrust);
        requiredAmounts.add(1);
        title = context.getString(R.string.strawberry_pie);
        calories = 150;
    }
}
