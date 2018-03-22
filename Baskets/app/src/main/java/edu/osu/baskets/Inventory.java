package edu.osu.baskets;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import edu.osu.baskets.foods.IFood;
import edu.osu.baskets.recipes.BaseRecipe;
import edu.osu.baskets.recipes.RecipeBook;

/**
 * Created by Alberto on 3/19/2018.
 */

public class Inventory {
    private Context mContext;
    private Container mBasket;
    private Container mFridge;
    private RecipeBook mRecipes;
    private String TAG = "Inventory";

    public Inventory(Context context) {
        mContext = context;
        int basketSize = mContext.getResources().getInteger(R.integer.basket_capacity);
        int fridgeSize = mContext.getResources().getInteger(R.integer.fridge_capacity);
        mBasket = new Container(mContext, basketSize);
        mFridge = new Container(mContext, fridgeSize);
    }

    public ArrayList<IFood> GetAllFoodItems() {
        ArrayList<IFood> allFood = new ArrayList<>(mBasket.GetSize() + mFridge.GetSize());
        allFood.addAll(mBasket.GetSlots());
        allFood.addAll(mFridge.GetSlots());
        return allFood;
    }
    public ArrayList<BaseRecipe> GetRecipes() {
        return mRecipes.getRecipes();
    }

    public boolean HasItem(String prefab, int amount) {
        int sum = mBasket.CountItem(prefab) + mFridge.CountItem(prefab);
        return (sum >= amount);
    }

    // returns how many will not have room for
    public int HasRoomInBasketFor(String prefab, int amount) {
        return mBasket.CountOverflowIfAdd(prefab, amount);
    }

    // returns item stack that doesn't fit (non-null)
    public IFood AddItemToBasket(IFood item) {
        int originalStack = item.GetStackSize();
        IFood overflow = mBasket.AddItem(item);
        if (!overflow.IsEmpty()) {
            Log.w(TAG, "Could not fit all items in Basket... Trying Fridge.");
            overflow = mFridge.AddItem(overflow);
        }
        if (!overflow.IsEmpty()) {
            Log.w(TAG, String.format("Could not fit [%d] out of [%d] items in Basket or Fridge.", overflow.GetStackSize(), originalStack));
        }
        return overflow;
    }

    public void MoveToBasket(int slotInFridge) {
        IFood item = mFridge.RemoveItemFromSlot(slotInFridge);
        IFood overflow = mBasket.AddItem(item);
        if (!overflow.IsEmpty()) {
            mFridge.AddItemToSlot(overflow, slotInFridge);
            Log.w(TAG, "Could not move all items to Basket.");
        }
    }
    public void MoveToFridge(int slotInBasket) {
        IFood item = mBasket.RemoveItemFromSlot(slotInBasket);
        IFood overflow = mFridge.AddItem(item);
        if (!overflow.IsEmpty()) {
            mBasket.AddItemToSlot(overflow, slotInBasket);
            Log.w(TAG, "Could not move all items to Fridge.");
        }
    }

    // returns removed item, with stackSize = amount
    public IFood RemoveItem(String prefab, int amount) {
        //asserts
        if (amount > mBasket.CountItem(prefab) + mFridge.CountItem(prefab)) { throw new AssertionError(); }
        if (amount > FoodUtils.Spawn(prefab).GetMaxStackSize()) { throw new AssertionError(); }

        int basketCount = mBasket.CountItem(prefab);
        if (basketCount >= amount) {
            //enough items in basket
            return mBasket.RemoveItem(prefab, amount);
        } else if (basketCount > 0) {
            //need some items from fridge
            IFood fridgeItem = mFridge.RemoveItem(prefab, amount - basketCount);
            mBasket.AddItem(fridgeItem);
            return mBasket.RemoveItem(prefab, amount);
        } else {
            //all items in fridge
            return mFridge.RemoveItem(prefab, amount);
        }
    }

    public void SwitchSlots(int slotFrom, boolean fromBasket, int slotTo, boolean toBasket) {
        Container fromContainer;
        Container toContainer;
        if (fromBasket) { fromContainer = mBasket; } else { fromContainer = mFridge; }
        if (toBasket) { toContainer = mBasket; } else { toContainer = mFridge; }

        IFood fromItem = fromContainer.RemoveItemFromSlot(slotFrom);
        IFood toItem = toContainer.RemoveItemFromSlot(slotTo);
        fromContainer.ForceItemInSlot(toItem, slotFrom);
        toContainer.ForceItemInSlot(fromItem, slotTo);
    }

    public void AgeByDays(int daysPassed) {
        mBasket.AgeItemsByDays(daysPassed);
    }

    /*public int CalculateRecipePoints() {
        int sum = 0;
        for (BaseRecipe recipe : mRecipes.getRecipes()) {
            sum += recipe.getPoints();
        }
        return sum;
    }*/
}
