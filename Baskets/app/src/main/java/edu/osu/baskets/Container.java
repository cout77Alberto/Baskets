package edu.osu.baskets;

import android.content.Context;

import java.util.ArrayList;

import edu.osu.baskets.foods.IFood;

/**
 * Created by Alberto on 3/19/2018.
 */

public class Container {
    private Context mContext;
    private ArrayList<IFood> mSlots;

    public Container(Context context, int capacity) {
        mContext = context;
        mSlots = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            mSlots.add(null);
        }
    }

    public ArrayList<IFood> GetSlots() {
        return mSlots;
    }

    public int CountItem(String prefabToCount) {
        int sum = 0;
        for (IFood slot : mSlots) {
            if (slot != null) {
                if (slot.GetPrefab().equals(prefabToCount)) {
                    sum += slot.GetStackSize();
                }
            }
        }
        return sum;
    }

    // can check amounts > maxStackSize
    // returns amount of items that don't fit
    public int HasRoomFor(String prefab, int amount) {
        int maxStack = FoodUtils.Spawn(prefab).GetMaxStackSize();
        for (IFood slot : mSlots) {
            if (slot == null) {
                amount -= maxStack;
            } else if (slot.GetPrefab().equals(prefab)) {
                amount -= (maxStack - slot.GetStackSize());
            }
        }
        if (amount > 0) {
            return amount;
        }
        return 0;
    }

    // finds all instances of "prefabToCount" in slots and
    // counts how many more items they can hold before reaching their maxStackSize
    private int CountAvailableStackSpaces(String prefabToCount) {
        int sum = 0;
        for (IFood slot : mSlots) {
            if (slot != null) {
                if (slot.GetPrefab().equals(prefabToCount)) {
                    sum += (slot.GetMaxStackSize() - slot.GetStackSize());
                }
            }
        }
        return sum;
    }

    //TODO: CountItemSmallestStack()
    // returns stack size of any non-full prefab stack, 0 otherwise
    /*private int CountItemSmallestStack(String prefab) {

    }*/

    // updates item
    // returns clone of item but with stackSize = amountToSeparate
    private IFood Split(IFood item, int amountToSeparate) {
        item.RemoveFromStackSize(amountToSeparate);
        IFood splitItem = item.Clone();
        splitItem.SetStackSize(amountToSeparate);
        return splitItem;
    }

    /**
     * Puts as many items from itemB into itemA.
     *
     * @requires itemA != null, itemB != null
     * @updates itemA
     * @clears itemB
     * @returns what doesn't fit in itemA
     **/
    private IFood CombineItems(IFood itemA, IFood itemB) {
        //assert
        if (!itemA.GetPrefab().equals(itemB.GetPrefab())) { throw new AssertionError(); }

        int stackA = itemA.GetStackSize();
        int stackB = itemB.GetStackSize();
        int maxStackSize = itemA.GetMaxStackSize();
        if (stackA + stackB <= maxStackSize) {
            //all items in B fit into A
            itemA.AddToStackSize(stackB);
            itemB.ClearStackSize();
            itemA.SetDaysToExpire(CombineSpoilage(itemA.GetDaysToExpire(), itemB.GetDaysToExpire(), stackA, stackB));
            return itemB;
        } else {
            //some items in B CANNOT fit into A
            IFood overflow = Split(itemB, stackA + stackB - maxStackSize);
            itemA.AddToStackSize(itemB.GetStackSize());
            itemA.SetDaysToExpire(CombineSpoilage(itemA.GetDaysToExpire(), itemB.GetDaysToExpire(), stackA, itemB.GetStackSize()));
            itemB.ClearStackSize();
            return overflow;
        }
    }

    private int CombineSpoilage(int expA, int expB, int stackA, int stackB) {
        float spoilage_round_up_thresh = Float.parseFloat(mContext.getResources().getString(R.string.spoilage_combination_thresh));
        float totalStack = stackA + stackB;
        float daysToExp = (expA * stackA/totalStack) + (expB * stackB/totalStack);
        float daysToExpTrunc = (float) Math.floor(daysToExp);

        if ((daysToExp - daysToExpTrunc) >= spoilage_round_up_thresh - 0.00001 ) {
            return (int) Math.ceil(daysToExp);
        } else {
            return (int) daysToExpTrunc;
        }
    }

    // NOTE: could exit loop as soon as both overflow and item reach stackSize = 0
    // returns all items that could not fit
    public IFood AddItem(IFood item) {
        int overflowCount = item.GetStackSize() - CountAvailableStackSpaces(item.GetPrefab());
        if (overflowCount < 0) { overflowCount = 0; }
        //overflow contains items that need to be placed in an empty slot
        IFood overflow = Split(item, overflowCount);

        for (int slot = 0; slot < mSlots.size(); slot++) {
            if (mSlots.get(slot) == null && !overflow.IsEmpty()) {
                mSlots.set(slot, overflow.Clone());
                overflow.ClearStackSize();
            } else if (mSlots.get(slot) != null) {
                if (mSlots.get(slot).GetPrefab().equals(item.GetPrefab()) && !item.IsEmpty()) {
                    item = CombineItems(mSlots.get(slot), item);
                }
            }
        }
        return overflow;
    }

    // if items same, add as many as can
    // if items different, replace item in position "slot"
    // returns overflow or item replaced (could be null)
    public IFood AddItemToSlot(IFood itemToAdd, int slot) {
        IFood slotItem = mSlots.get(slot);
        if (slotItem == null) {
            //no item, use slot
            return mSlots.set(slot, itemToAdd);
        } else if (slotItem.GetPrefab().equals(itemToAdd.GetPrefab())) {
            //same items, combine
            return CombineItems(slotItem, itemToAdd);
        } else {
            //different items, replace
            return mSlots.set(slot, itemToAdd);
        }
    }

    // TODO: refactor RemoveItem to include cases where multiple slots have same item
    // NOTE: assumes restriction that only one slot in a container can have a specific item
    // returns item removed, with stackSize = amount
    public IFood RemoveItem(String prefab, int amount) {
        //asserts
        if (amount > CountItem(prefab)) { throw new AssertionError(); }
        if (amount <= 0) { throw new AssertionError(); }

        IFood removed = null;
        for (int slot = 0; slot < mSlots.size(); slot++) {
            if (mSlots.get(slot) != null) {
                if (mSlots.get(slot).GetPrefab().equals(prefab)) {
                    removed = Split(mSlots.get(slot), amount);
                    if (mSlots.get(slot).IsEmpty()) { mSlots.set(slot, null); }
                    break;
                }
            }
        }
        return removed;
    }

    // returns item in position "slot"
    public IFood RemoveItemFromSlot(int slot) {
        return mSlots.set(slot, null);
    }

    public void AgeItemsByDays(int numDays) {
        for (int slot = 0; slot < mSlots.size(); slot++) {
            mSlots.get(slot).AgeByDays(numDays);
            if (mSlots.get(slot).IsExpired()) { mSlots.set(slot, null); }
        }
    }
}
