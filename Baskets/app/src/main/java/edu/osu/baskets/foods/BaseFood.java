package edu.osu.baskets.foods;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

//TODO: Make FoodItem an interface, then a "BaseFood"
public class BaseFood implements IFoodItem {
    protected Context mContext;
    protected String prefab;
    protected String name;
    protected String description;
    protected int calories;
    protected int stackSize;
    protected int daysToExpiration;
    protected List<String> tags;

    public BaseFood(Context context){
        mContext = context;
        prefab = "";
        name = "";
        description = "";
        calories = 0;
        stackSize = 1;
        daysToExpiration = context.getResources().getInteger(R.integer.expiration_days_default);
        tags = new LinkedList<>();
    }

    public void AddTag(String tag) {
        tags.add(tag);
    }

    public void SetPrefab(String pref) { prefab = pref; }
    public String GetPrefab() { return prefab; }

    public String GetName() {
        return mContext.getString(mContext.getResources().getIdentifier(prefab, "string", "edu.osu.baskets"));
    }
    public String GetDescription() {
        return mContext.getString(mContext.getResources().getIdentifier(prefab+"_desc", "string", "edu.osu.baskets"));
    }

    public void SetCalories(int calories) {
        this.calories = calories;
    }
    public int GetCalories() {
        return calories;
    }

    public void AddToStackSize(int numToAdd) {
        stackSize += numToAdd;
    }
    public void RemoveFromStackSize(int numToRemove) {
        stackSize -= numToRemove;
    }
    public int GetStackSize() {
        return stackSize;
    }

    public void SetDaysToExpire(int days) {
        daysToExpiration = days;
    }
    public int GetDaysToExpire() {
        return daysToExpiration;
    }

    public void onAcquired(String acquirer) { }

    public void onEaten(String eater) {
        //eater.calorieScore += calories;
    }
    public void onPostEaten(String eater) { }

    public void onExpired() { }
}
