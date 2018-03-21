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
        //TODO
        Log.e(TAG, "Method not implemented.");
        return new ArrayList<>();
    }
    public ArrayList<BaseRecipe> GetRecipes() {
        return mRecipes.getRecipes();
    }

    public boolean HasItem(String prefab, int amount) {
        int sum = mBasket.CountItem(prefab) + mFridge.CountItem(prefab);
        return (sum >= amount);
    }

    public int HasRoomInBasketFor(String prefab, int amount) {
        return mBasket.HasRoomFor(prefab, amount);
    }

    // TODO: automatically add item to fridge if doesn't fit in basket
    public boolean AddItemToBasket(IFood item) {
        IFood overflow = mBasket.AddItem(item);
        if (!overflow.IsEmpty()) {
            Log.w(TAG, "Could not fit all items in Basket.");
            return false;
        }
        return true;
    }

    /*TODO: public boolean AddItemToBasketInSlot(IFood item, int slot) {

    }*/

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

    // TODO: refactor RemoveItem to include cases where multiple slots have same item
    // NOTE: assumes restriction that only one slot in a container can have a specific item
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

    //TODO: SwitchSlots(int slotFrom, int slotTo, boolean forBasket)

    public void AgeByDays(int daysPassed) {
        mBasket.AgeItemsByDays(daysPassed);
    }

    public int CalculateRecipePoints() {

        return 0;
    }
}
