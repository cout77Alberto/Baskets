package edu.osu.baskets.recipes;

import java.util.ArrayList;

import edu.osu.baskets.foods.BaseFood;

/**
 * Created by Daniel on 3/9/2018.
 */

public class BaseRecipe {
    private int calories;
    private String title = "Strawberry Shake";
    ArrayList<BaseFood> requiredFoods = new ArrayList<BaseFood>();

    public Boolean isCookable(){
        boolean cookable = false;
        //TODO needs to check for necessary food items
        return cookable;
    }
    public int cook(){
        return calories;
    }
    public String getTitle(){
        return title;
    }
    public  void setTitle(String t){
        title = t;
    }
}
