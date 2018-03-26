package edu.osu.baskets.recipes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import edu.osu.baskets.AccountSingleton;
import edu.osu.baskets.Inventory;
import edu.osu.baskets.R;
import edu.osu.baskets.foods.BaseFood;

/**
 * Created by Daniel on 3/9/2018.
 */

public class BaseRecipe {
    protected int calories;
    protected String title;
    ArrayList<BaseFood> requiredFoods = new ArrayList<BaseFood>();
    ArrayList<Integer> requiredAmounts = new ArrayList<>();

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

    public Boolean isCookable(Context context){
        boolean cookable = false, missingReqs=false;
        //TODO needs to check for necessary food items
        for(int i=0;i<requiredFoods.size()&&!missingReqs;i++){
            if(!Inventory.get(context).HasItem(requiredFoods.get(i).GetPrefab(), requiredAmounts.get(i))){
                missingReqs = true;
                cookable = false;
            }
        }
        if(!missingReqs){
            cookable = true;
        }
        return cookable;
    }

    public String getTitle(){
        return title;
    }
    public  void setTitle(String t){
        title = t;
    }
    public void make(Context context){
        //add calories to account
        AccountSingleton account = AccountSingleton.get();
        account.addCalories(calories);
        account.pushAccount();
        //remove food from inventory
        Inventory inventory = Inventory.get(context);
        for (int i = 0; i < requiredFoods.size(); i++) {
            inventory.RemoveItem(requiredFoods.get(i).GetPrefab(),requiredAmounts.get(i));
        }
        //add to history
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
