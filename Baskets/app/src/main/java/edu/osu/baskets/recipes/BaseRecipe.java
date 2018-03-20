package edu.osu.baskets.recipes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import edu.osu.baskets.foods.BaseFood;

/**
 * Created by Daniel on 3/9/2018.
 */

public class BaseRecipe {
    private int calories;
    private String title = "Strawberry Shake";
    ArrayList<BaseFood> requiredFoods = new ArrayList<BaseFood>();

    public Date getLastCreated() {
        if(LastCreated==null){
            Date defDate = new Date();
            defDate.setTime(0);
            return defDate;
        }
        return LastCreated;
    }

    public void setLastCreated(Date lastCreated) {
        LastCreated = lastCreated;
    }

    private Date LastCreated;
    //TODO consider amount

    public Boolean isCookable(){
        boolean cookable = false;
        //TODO needs to check for necessary food items
        int i = Integer.parseInt(title.substring(8));
        if(i%2==0){
            cookable = true;
        }
        return true;
    }

    public String getTitle(){
        return title;
    }
    public  void setTitle(String t){
        title = t;
    }
    public void make(Context context){
        //add calories to account
        CookingHistory.get(context).add(this.copy());
    }
    public BaseRecipe copy(){
        BaseRecipe copy = new BaseRecipe();
        copy.setTitle(title);
        copy.calories = calories;
        copy.setLastCreated(new Date());
        return copy;
    }
}
