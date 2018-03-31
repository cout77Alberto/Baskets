package edu.osu.baskets.foods;

import android.content.Context;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import edu.osu.baskets.FoodUtils;
import edu.osu.baskets.R;

/**
 * Created by Alberto on 2/26/2018.
 */

public class BaseFood implements IFood {
    protected Context mContext;
    protected String mPrefab;
    protected String mName;
    protected String mDescription;
    protected int mCalories;
    protected int mStackSize;
    protected int mMaxStackSize;
    protected int mDaysToExpiration;
    protected int mShelfLife;
    protected List<String> mTags;
    private String TAG = "BaseFood";

    public BaseFood(Context context){
        mContext = context;
        mPrefab = "";
        mName = "";
        mDescription = "";
        mCalories = 0;
        mStackSize = 1;
        mShelfLife = context.getResources().getInteger(R.integer.shelf_life_default);
        mDaysToExpiration = mShelfLife;
        mTags = new LinkedList<>();
    }

    public void AddTag(String tag) {
        mTags.add(tag);
    }

    public void SetPrefab(String pref) { mPrefab = pref; }
    public String GetPrefab() { return mPrefab; }

    public String GetName() {
        return mContext.getString(mContext.getResources().getIdentifier(mPrefab, "string", "edu.osu.baskets"));
    }
    public String GetDescription() {
        return mContext.getString(mContext.getResources().getIdentifier(mPrefab +"_desc", "string", "edu.osu.baskets"));
    }
    public int GetImageResId() {
        return mContext.getResources().getIdentifier(mPrefab, "drawable","edu.osu.baskets");
    }

    public void SetCalories(int calories) {
        this.mCalories = calories;
    }
    public int GetCalories() {
        return mCalories;
    }

    public void SetMaxStackSize(int maxStackSize) { mMaxStackSize = maxStackSize; }
    public int GetMaxStackSize() { return mMaxStackSize; }
    public void SetStackSize(int stackSize) {
        if (stackSize > mMaxStackSize) {
            Log.e(TAG, String.format("Attempted to exceed max stack size, %d, of prefab '%s'", mMaxStackSize, mPrefab));
            stackSize = mMaxStackSize;
        }
        mStackSize = stackSize;
    }
    public void ClearStackSize() { mStackSize = 0; }
    public void AddToStackSize(int numToAdd) {
        mStackSize += numToAdd;
    }
    public void RemoveFromStackSize(int numToRemove) {
        mStackSize -= numToRemove;
    }
    public int GetStackSize() {
        return mStackSize;
    }
    public boolean IsEmpty() { return mStackSize == 0; }
    public boolean IsFull() { return mStackSize == mMaxStackSize; }

    public void SetShelfLifeAndResetExpireCounter(int days) {
        SetShelfLife(days);
        mDaysToExpiration = mShelfLife;
    }
    public void SetShelfLife(int days) {
        mShelfLife = days;
        if (mShelfLife <= 0) {
            onExpired();
        } else if (mDaysToExpiration > days){
            mDaysToExpiration = mShelfLife;
        }
    }
    public int GetShelfLife() { return mShelfLife; }
    public void SetDaysToExpire(int days) {
        mDaysToExpiration = days;
        if (mDaysToExpiration <= 0) {
            onExpired();
        } else if (mDaysToExpiration > mShelfLife) {
            mDaysToExpiration = mShelfLife;
        }
    }
    public int GetDaysToExpire() {
        return mDaysToExpiration;
    }
    public void AgeByDays(int days) {
        mDaysToExpiration -= days;
        if (mDaysToExpiration <= 0) {
            onExpired();
        }
    }
    public boolean IsExpired() { return mDaysToExpiration == 0; }

    public void onAcquired(String acquirer) { }

    public void onEaten(String eater) {
        //eater.calorieScore += mCalories;
    }
    public void onPostEaten(String eater) { }

    public void onExpired() { }

    @Override
    public String toString() {
        // data format: [prefab, stacksize, days-to-expire]
        String data = "";
        data = data.concat(mPrefab + ",");
        data = data.concat(Integer.toString(mStackSize) + ",");
        data = data.concat(Integer.toString(mDaysToExpiration));
        return data;
    }
    public IFood Clone() {
        IFood clone = FoodUtils.Spawn(this.GetPrefab());
        clone.SetStackSize(mStackSize);
        clone.SetShelfLife(mShelfLife);
        clone.SetDaysToExpire(mDaysToExpiration);
        return clone;
    }
}
