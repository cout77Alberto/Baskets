package edu.osu.baskets.recipes;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Daniel on 3/9/2018.
 */

public class CookingHistory {

    private static CookingHistory sCookingHistory;
    private ArrayList<BaseRecipe> mRecipes = new ArrayList<BaseRecipe>();
    public static CookingHistory get(Context context) {
        if (sCookingHistory == null) {
            sCookingHistory = new CookingHistory(context);
        }
        return sCookingHistory;
    }

    private CookingHistory(Context context) {
        mRecipes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            BaseRecipe recipe = new BaseRecipe();
            recipe.setTitle("History #" + i);
            mRecipes.add(recipe);
        }
    }

    public void share(){}
    public boolean hasRecipe(BaseRecipe r){
        return mRecipes.contains(r);
    }
    public ArrayList<BaseRecipe> getRecipes(){
        return  mRecipes;
    }
    public BaseRecipe getCrime(String title) {
        for (BaseRecipe recipe : mRecipes) {
            if (recipe.getTitle().equals(title)) {
                return recipe;
            }
        }
        return null;
    }
}
