package edu.osu.baskets.foods;

/**
 * Created by Alberto on 2/26/2018.
 */

//Make IFood extend "Taggable"?
public interface IFood {
    public void AddTag(String tag);

    public void SetPrefab(String pref);
    public String GetPrefab();

    public String GetName();
    public String GetDescription();

    public void SetCalories(int calories);
    public int GetCalories();

    public void SetMaxStackSize(int maxStackSize);
    public int GetMaxStackSize();
    public void SetStackSize(int stackSize);
    public void ClearStackSize();
    public void AddToStackSize(int numToAdd);
    public void RemoveFromStackSize(int numToRemove);
    public int GetStackSize();
    public boolean IsEmpty();
    public boolean IsFull();

    public void SetDaysToExpire(int days);
    public void AgeByDays(int days);
    public int GetDaysToExpire();
    public boolean IsExpired();

    public void onAcquired(String acquirer);

    public void onEaten(String eater);
    public void onPostEaten(String eater);

    public void onExpired();

    public IFood Clone();
}
