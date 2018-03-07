package edu.osu.baskets.foods;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alberto on 2/26/2018.
 */

//Make IFoodItem extend "Taggable"?
public interface IFoodItem {
    public void AddTag(String tag);

    public void SetPrefab(String pref);
    public String GetPrefab();

    public String GetName();
    public String GetDescription();

    public void SetCalories(int calories);
    public int GetCalories();

    public void AddToStackSize(int numToAdd);
    public void RemoveFromStackSize(int numToRemove);
    public int GetStackSize();

    public void SetDaysToExpire(int days);
    public int GetDaysToExpire();

    public void onAcquired(String acquirer);

    public void onEaten(String eater);
    public void onPostEaten(String eater);

    public void onExpired();
}
