package edu.osu.baskets.recipes;

import android.content.Context;

import java.util.ArrayList;


/**
 * Created by Daniel on 3/9/2018.
 */

public class RecipeBook {

    private static RecipeBook sRecipeBook;
    private ArrayList<BaseRecipe> mRecipes = new ArrayList<BaseRecipe>();
    public static RecipeBook get(Context context) {
        if (sRecipeBook == null) {
            sRecipeBook = new RecipeBook(context);
        }
        return sRecipeBook;
    }

    private RecipeBook(Context context) {
        mRecipes = new ArrayList<>();
        mRecipes.add(new StrawberryShakeRecipe(context));
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
